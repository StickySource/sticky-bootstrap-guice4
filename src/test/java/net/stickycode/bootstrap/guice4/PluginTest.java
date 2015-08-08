package net.stickycode.bootstrap.guice4;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.util.Types;

import net.stickycode.bootstrap.guice3.StickyGuice;
import net.stickycode.bootstrap.tck.plugin.AbstractPluggableTest;
import net.stickycode.bootstrap.tck.plugin.PluggableContract;

public class PluginTest
    extends AbstractPluggableTest {

  @Before
  public void setup() {
    Injector injector = StickyGuice.createApplicationInjector(getClass().getPackage().getName());
    injector.injectMembers(this);
  }

  @Inject
  Injector injector;

  @Test
  public void keys() {
    injector.getBinding(Key.get(Types.setOf(PluggableContract.class)));
  }
}
