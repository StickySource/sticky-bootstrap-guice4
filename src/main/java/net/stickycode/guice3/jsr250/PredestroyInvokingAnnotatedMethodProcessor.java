package net.stickycode.guice3.jsr250;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PreDestroy;

import net.stickycode.reflector.AnnotatedMethodProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredestroyInvokingAnnotatedMethodProcessor
   extends AnnotatedMethodProcessor {

  private Logger log = LoggerFactory.getLogger(getClass());

  private Set<Object> seen = new HashSet<Object>();

  public PredestroyInvokingAnnotatedMethodProcessor() {
    super(PreDestroy.class);
  }

  @Override
  public void processMethod(Object target, Method method) {
    if (seen.contains(target))
      return;

    try {
      method.invoke(target, new Object[0]);
    }
    catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    catch (InvocationTargetException e) {
      log.error("failed to destroy {}", e.getCause());
      throw new RuntimeException(e);
    }

    log.info("predestroy {}.{}", target.getClass().getName(), method.getName());
    seen.add(target);
  }

}
