package net.stickycode.bootstrap.tck.plugin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.inject.Inject;

import net.stickycode.bootstrap.tck.AbstractBootstrapTest;

import org.junit.Test;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.util.Types;

public class PluggableTest
    extends AbstractBootstrapTest {

  @Inject
  Set<Pluggable> plugged;

  @Inject
  Set<PluggableContract> abstractInTheMiddle;

  @Inject
  Set<GenericPluggable<?>> genericPlugged;

  @SuppressWarnings("rawtypes")
  @Inject
  Set<GenericPluggable> genericPluggedNoWildcard;

  @Inject
  Injector injector;

  @Test
  public void verify() {
    assertThat(plugged).hasSize(2);
    assertThat(genericPluggedNoWildcard).hasSize(3);
    assertThat(genericPlugged).hasSize(3);

    injector.getBinding(Key.get(Types.setOf(PluggableContract.class)));
  }
}
