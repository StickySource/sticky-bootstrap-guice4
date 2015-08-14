package net.stickycode.bootstrap.guice4;

import javax.inject.Inject;

import com.google.inject.Injector;

import net.stickycode.bootstrap.ComponentContainer;
import net.stickycode.stereotype.StickyComponent;

@StickyComponent
public class Guice4ComponentContainer
    implements ComponentContainer {

  @Inject
  private Injector injector;

  @Override
  public void inject(Object value) {
    injector.injectMembers(value);
  }

  @Override
  public <T> T find(Class<T> type) {
    return injector.getInstance(type);
  }

}
