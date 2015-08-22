package net.stickycode.bootstrap.guice4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.stickycode.bootstrap.StickyBootstrap;

public class Guice4StickyBootstrap
    implements StickyBootstrap {

  private Logger log = LoggerFactory.getLogger(getClass());

  private Injector injector;

  @Override
  public StickyBootstrap scan(String... packages) {
    log.debug("scanning {}", new Object[] { packages });
    FastClasspathScanner scanner = new FastClasspathScanner(packages).scan();

    this.injector = Guice.createInjector(new StickyModule(scanner));
    return this;
  }

  @Override
  public StickyBootstrap inject(Object instance) {
    try {
      injector.injectMembers(instance);
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

  @Override
  public <T> T find(Class<T> type) {
    return injector.getInstance(type);
  }

}
