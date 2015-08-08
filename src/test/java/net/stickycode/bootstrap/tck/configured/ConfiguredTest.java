package net.stickycode.bootstrap.tck.configured;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import net.stickycode.bootstrap.tck.AbstractBootstrapTest;

import org.junit.Ignore;
import org.junit.Test;

public class ConfiguredTest
    extends AbstractBootstrapTest {

  @Inject
  AConfiguredTuple tuple;

  @Inject
  AConfiguredTuple tuple2;

  @Inject
  ConfguredTuple tupleContract;

  @Test
  @Ignore("I think configured tuples will disappear soon, they current method of configuration is broken")
  public void verify() {
    assertThat(tuple).isNotSameAs(tupleContract);
    assertThat(tuple).isNotSameAs(tuple2);
  }
}
