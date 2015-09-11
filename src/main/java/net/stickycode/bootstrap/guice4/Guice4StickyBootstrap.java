package net.stickycode.bootstrap.guice4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.ProvisionException;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.stickycode.bootstrap.StickyBootstrap;
import net.stickycode.guice3.jsr250.Jsr250Module;

public class Guice4StickyBootstrap
    implements StickyBootstrap {

  private Logger log = LoggerFactory.getLogger(getClass());

  private List<String> packages = new ArrayList<>();

  private List<Module> modules = new ArrayList<>();

  private BootstrapMetadata metadata = new BootstrapMetadata();

  private Injector injector;

  private Object $lock = new Object();

  @Override
  public StickyBootstrap scan(String... scan) {
    List<String> list = Arrays.asList(scan);
    return scan(list);
  }

  @Override
  public StickyBootstrap scan(Collection<String> list) {
    synchronized ($lock) {
      if (injector != null)
        throw new ScanningIsAlreadyCompleteFailure();

      packages.addAll(list);

    }

    return this;
  }

  @Override
  public StickyBootstrap inject(Object instance) {
    try {
      getInjector().injectMembers(instance);
    }
    catch (ProvisionException e) {
      if (e.getCause() instanceof RuntimeException) {
        log.error("Unrolling provision failure {}", e.getMessage());
        throw (RuntimeException) e.getCause();
      }
      throw e;
    }
    return this;
  }

  private Injector getInjector() {
    synchronized ($lock) {
      if (injector == null) {
        log.debug("scanning {}", packages);
        FastClasspathScanner scanner = new FastClasspathScanner(packages.toArray(new String[packages.size()])).scan();
        StickyModule stickyModule = new StickyModule(scanner);
        BootstrapMetadataModule metadataModule = new BootstrapMetadataModule(metadata);
        List<Module> m = new ArrayList<>();
        m.add(stickyModule);
        m.add(metadataModule);
        m.addAll(modules);
        this.injector = Guice.createInjector(m);
      }

      return injector;
    }
  }

  @Override
  public <T> T find(Class<T> type) {
    return getInjector().getInstance(type);
  }

  @Override
  public boolean canFind(Class<?> type) {
    return getInjector().getExistingBinding(Key.get(type)) != null;
  }

  @Override
  public Object getImplementation() {
    return getInjector();
  }

  @Override
  public void registerSingleton(String beanName, Object bean, Class<?> type) {
    metadata.registerBean(beanName, bean, type);
  }

  @Override
  public void registerType(String beanName, Class<?> type) {
    metadata.registerType(beanName, type);
  }

  @Override
  public void shutdown() {
    if (injector != null)
      Jsr250Module.preDestroy(log, injector);
  }

  @Override
  public void extend(Object extension) {
    if (extension instanceof Module)
      modules.add((Module) extension);
    else
      throw new UnknownExtensionFailure(extension);
  }
}
