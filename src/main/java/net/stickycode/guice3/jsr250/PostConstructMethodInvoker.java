package net.stickycode.guice3.jsr250;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import net.stickycode.reflector.AnnotatedMethodProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostConstructMethodInvoker
    extends AnnotatedMethodProcessor
{

  private Logger log = LoggerFactory.getLogger(getClass());

  public PostConstructMethodInvoker() {
    super(PostConstruct.class);
  }

  @Override
  public void processMethod(Object target, Method method) {
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
      log.error("failed to post construct {} with {}", new Object[] {target.getClass(), method, e.getCause()});
      throw new PostConstructionFailureException(target, method, e.getCause());
    }
  }

}
