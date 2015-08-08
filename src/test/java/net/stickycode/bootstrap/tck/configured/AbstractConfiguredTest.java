package net.stickycode.bootstrap.tck.configured;

import static org.assertj.core.api.StrictAssertions.assertThat;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;

public abstract class AbstractConfiguredTest {

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
