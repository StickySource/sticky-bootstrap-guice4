package net.stickycode.bootstrap.guice4;

import java.util.List;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class StickyFrameworkModule
    extends AbstractStickyModule {

  public StickyFrameworkModule(FastClasspathScanner scanner) {
    super(scanner);
  }

    @Override
    public void configure() {
      binder().requireExplicitBindings();
      List<String> framework = getFrameworkNames();
      List<String> componentNames = getComponentNames();
      framework.retainAll(componentNames);
      debug("framework {}", framework);
      load(framework);
    }

}
