package net.stickycode.bootstrap.guice4;

import com.google.inject.Injector;

import net.stickycode.bootstrap.StickyBootstrap;

public class GuiceStickyBootstrap
    implements StickyBootstrap {

  private Injector injector;

  public GuiceStickyBootstrap() {
    this.injector = StickyGuice.createInjector("net.stickycode");
  }

  @Override
  public StickyBootstrap scan(String... packages ) {
    this.injector = StickyGuice.createInjector(injector, packages);
    return this;
  }

  @Override
  public void inject(Object instance) {
    injector.injectMembers(instance);
  }

  @Override
  public <T> T find(Class<T> type) {
    return injector.getInstance(type);
  }

}
