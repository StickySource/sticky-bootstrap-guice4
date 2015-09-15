package net.stickycode.bootstrap.guice4;

import java.util.List;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class StickyApplicationModule
    extends AbstractStickyModule {

  public StickyApplicationModule(FastClasspathScanner scanner) {
    super(scanner);
  }

  @Override
  public void configure() {
    binder().requireExplicitBindings();
    List<String> framework = getFrameworkNames();
    List<String> componentNames = getComponentNames();
    componentNames.removeAll(framework);
    debug("components {}", framework);
    load(componentNames);
  }

}
