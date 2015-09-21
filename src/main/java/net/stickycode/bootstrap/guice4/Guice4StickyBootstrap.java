package net.stickycode.bootstrap.guice4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.ProvisionException;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.stickycode.bootstrap.StickyBootstrap;
import net.stickycode.bootstrap.StickySystemStartup;

public class Guice4StickyBootstrap
    implements StickyBootstrap {
  static Boolean tellMeWhatsGoingOn;

  private Logger log = LoggerFactory.getLogger(getClass());

  static {
    tellMeWhatsGoingOn = new Boolean(System.getProperty("sticky.bootstrap.debug", "false"));
    if (!tellMeWhatsGoingOn)
      LoggerFactory.getLogger(Guice4StickyBootstrap.class).debug("Enable binding trace with -Dsticky.bootstrap.debug=true");
  }

  private List<String> packages = new ArrayList<>();

  private List<Module> modules = new ArrayList<>();

  private BootstrapMetadata metadata = new BootstrapMetadata();

  private Injector injector;

  private Object $lock = new Object();

  private Injector parentInjector;

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
        List<Module> m = new ArrayList<>();

        m.add(new BootstrapMetadataModule(metadata));
        m.addAll(modules);
        if (!packages.isEmpty()) {
          log.debug("scanning {}", packages);
          FastClasspathScanner scanner = new FastClasspathScanner(packages.toArray(new String[packages.size()])).scan();
          parentInjector = Guice.createInjector(new StickyFrameworkModule(scanner));
          m.add(new StickyApplicationModule(scanner));
          this.injector = parentInjector.createChildInjector(m);
        }
        else
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
    if (injector != null) {
      if (injector.getExistingBinding(Key.get(StickySystemStartup.class)) != null)
        injector.getInstance(StickySystemStartup.class).shutdown();
    }
  }

  @Override
  public void extend(Object extension) {
    if (extension instanceof Module)
      modules.add((Module) extension);
    else
      throw new UnknownExtensionFailure(extension);
  }

  @Override
  public void start() {
    Injector i = getInjector();
    if (i.getExistingBinding(Key.get(StickySystemStartup.class)) != null)
      i.getInstance(StickySystemStartup.class).start();
  }

  @Override
  public void registerProvider(String name, Provider<Object> provider, Class<?> type) {
    metadata.registerProvider(name, provider, type);
  }
}
