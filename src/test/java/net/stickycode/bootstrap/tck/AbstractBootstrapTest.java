package net.stickycode.bootstrap.tck;

import net.stickycode.bootstrap.guice3.StickyGuice;

import org.junit.Before;

import com.google.inject.Injector;

public abstract class AbstractBootstrapTest {

  @Before
  public void setup() {
    Injector injector = StickyGuice.createApplicationInjector(getClass().getPackage().getName());
    injector.injectMembers(this);
  }

}
