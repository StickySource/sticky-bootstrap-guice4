package net.stickycode.bootstrap.guice4;

import static net.stickycode.bootstrap.guice4.StickyModule.applicationModule;
import static net.stickycode.bootstrap.guice4.StickyModule.bootstrapModule;
import static net.stickycode.bootstrap.guice4.StickyModule.keyBuilderModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

import net.stickycode.bootstrap.StickyBootstrap;

public class Guice4StickyBootstrap
    implements StickyBootstrap {

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
    this.injector =
        injector.createChildInjector(
            applicationModule(packages));
    return this;
  }

  @Override
  public StickyBootstrap inject(Object instance) {
    injector.injectMembers(instance);
    return this;
  }

  @Override
  public <T> T find(Class<T> type) {
    return injector.getInstance(type);
  }

}
