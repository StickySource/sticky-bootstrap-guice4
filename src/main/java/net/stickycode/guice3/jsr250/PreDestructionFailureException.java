package net.stickycode.guice3.jsr250;

import java.lang.reflect.Method;

import net.stickycode.exception.PermanentException;

@SuppressWarnings("serial")
public class PreDestructionFailureException
    extends PermanentException {

  public PreDestructionFailureException(Object target, Method method, Throwable cause) {
    super(cause, "Failed with '' to call pre destroy on {}", cause, method);
  }

}
