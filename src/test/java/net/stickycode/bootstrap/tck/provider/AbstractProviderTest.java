package net.stickycode.bootstrap.tck.provider;

import static org.assertj.core.api.StrictAssertions.assertThat;

import javax.inject.Inject;
import javax.inject.Provider;

import org.junit.Test;

import net.stickycode.bootstrap.tck.component.StandardComponent;

public abstract class AbstractProviderTest {

  @Inject
  Something something;

  @Inject
  Provider<Something> somethingProvider;

  @Inject
  Provider<StandardComponent> componentProvider;

  @Test
  public void ensureProvidersReturnDifferentValues() {
    // even though the provider has not scope its the same at each injection point
    // seems counter intuitive, I would expect a provider to be singleton scoped
    // in order to be the same at all injection points
    assertThat(somethingProvider.get()).isNotSameAs(somethingProvider.get());
    assertThat(something).isNotSameAs(somethingProvider.get());
  }

  @Test
  public void ensureStandardComponentProvidersReturnSingletonsAsExpected() {
    assertThat(componentProvider).isNotNull();
    assertThat(componentProvider.get()).isSameAs(componentProvider.get());
  }
}
