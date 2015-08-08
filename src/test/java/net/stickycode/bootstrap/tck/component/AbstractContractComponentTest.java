package net.stickycode.bootstrap.tck.component;

import static org.assertj.core.api.StrictAssertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;

public abstract class AbstractContractComponentTest {

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
