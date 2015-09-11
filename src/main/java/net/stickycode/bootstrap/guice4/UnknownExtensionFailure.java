package net.stickycode.bootstrap.guice4;

import com.google.inject.Module;

import net.stickycode.stereotype.failure.ParameterisedFailure;

@SuppressWarnings("serial")
public class UnknownExtensionFailure
    extends ParameterisedFailure {

  public UnknownExtensionFailure(Object extension) {
    super("Expected a {} but got {}", Module.class.getName(), extension.getClass().getName());
  }

}
