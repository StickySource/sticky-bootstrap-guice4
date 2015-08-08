package net.stickycode.bootstrap.guice4;

import org.junit.Before;

import com.google.inject.Injector;

import net.stickycode.bootstrap.guice3.StickyGuice;
import net.stickycode.bootstrap.tck.plugin.AbstractPluggableTest;

public class PluginTest
    extends AbstractPluggableTest {

  @Before
  public void setup() {
    Injector injector = StickyGuice.createApplicationInjector(getClass().getPackage().getName());
    injector.injectMembers(this);
  }
}
