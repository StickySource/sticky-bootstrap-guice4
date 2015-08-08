package net.stickycode.guice3.jsr250;

import java.lang.reflect.Method;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class PostConstructionFailureException
    extends PermanentException {

  public PostConstructionFailureException(Object target, Method method, Throwable cause) {
    super(cause, "Failed with '' to call post construct on {}", cause, method);
  }

}
