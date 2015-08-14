package net.stickycode.bootstrap.guice4;

import static net.stickycode.bootstrap.guice4.StickyModule.applicationModule;
import static net.stickycode.bootstrap.guice4.StickyModule.bootstrapModule;
import static net.stickycode.bootstrap.guice4.StickyModule.keyBuilderModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;

import net.stickycode.bootstrap.StickyBootstrap;

public class Guice4StickyBootstrap
    implements StickyBootstrap {

  private Logger log = LoggerFactory.getLogger(getClass());

  private Injector injector;

  public Guice4StickyBootstrap() {
    this.injector = Guice.createInjector(
        bootstrapModule(),
        keyBuilderModule());
  }

  public static Injector createInjector(Injector injector, String... packages) {
    return injector
        .createChildInjector(
            applicationModule(packages));
  }

  @Override
  public StickyBootstrap scan(String... packages) {
    this.injector = injector.createChildInjector(
        applicationModule(packages));
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
