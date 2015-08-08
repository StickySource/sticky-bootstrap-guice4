package net.stickycode.guice3.jsr250;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import net.stickycode.bootstrap.AbstractStickySystem;
import net.stickycode.bootstrap.StickySystem;
import net.stickycode.stereotype.plugin.StickyExtension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

@StickyExtension
public class GuiceJsr250Subsystem
    extends AbstractStickySystem {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  Injector injector;

  @Override
  public void shutdown() {
    assert injector != null;
    Jsr250Module.preDestroy(log, injector);
  }

  @Override
  public boolean uses(StickySystem subsystem) {
    return false;
  }

  @Override
  public boolean isUsedBy(StickySystem subsystem) {
    return true;
  }

  @Override
  public String getLabel() {
    return PreDestroy.class.getSimpleName();
  }

}
