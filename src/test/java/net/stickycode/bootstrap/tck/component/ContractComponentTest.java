package net.stickycode.bootstrap.tck.component;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import net.stickycode.bootstrap.tck.AbstractBootstrapTest;

import org.junit.Test;

public class ContractComponentTest
    extends AbstractBootstrapTest {

  @Inject
  Hydra concrete;

  @Inject
  OneUp oneUp;

  @Inject
  TwoUp twoUp;

  @Inject
  ThreeUp threeUp;

  @Inject
  FourUp fourUp;

  @Inject
  FiveUp fiveUp;

  @Inject
  SixUp sixUp;

  @Test
  public void implementationAndContractAreTheSameInstance() {
    assertThat(concrete).isSameAs(oneUp);
    assertThat(concrete).isSameAs(twoUp);
    assertThat(concrete).isSameAs(threeUp);
    assertThat(concrete).isSameAs(fourUp);
    assertThat(concrete).isSameAs(fiveUp);
  }

}
