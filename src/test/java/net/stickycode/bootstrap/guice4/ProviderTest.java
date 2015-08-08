package net.stickycode.bootstrap.guice4;

import static org.assertj.core.api.StrictAssertions.assertThat;

import javax.inject.Inject;
import javax.inject.Provider;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;

import net.stickycode.bootstrap.guice3.StickyGuice;
import net.stickycode.bootstrap.tck.provider.AbstractProviderTest;
import net.stickycode.bootstrap.tck.provider.Something;

public class ProviderTest
    extends AbstractProviderTest {

  @Inject
  Something something;

  @Inject
  Provider<Something> somethingProvider;

  @Before
  public void setup() {
    Injector injector = StickyGuice.createApplicationInjector(getClass().getPackage().getName());
    injector.injectMembers(this);
  }

  @Test
  public void ensureProvidersReturnDifferentValues() {
    // even though the provider has no scope its the same at each injection point
    // seems counter intuitive, I would expect a provider to be singleton scoped
    // in order to be the same at all injection points
    assertThat(somethingProvider).isNotNull();
    assertThat(somethingProvider.get()).isNotSameAs(somethingProvider.get());
    assertThat(something).isNotSameAs(somethingProvider.get());
  }

}
