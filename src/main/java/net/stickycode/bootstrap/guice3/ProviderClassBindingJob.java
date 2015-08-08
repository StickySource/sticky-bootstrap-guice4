package net.stickycode.bootstrap.guice3;

import com.google.inject.Scope;

import de.devsurf.injection.guice.install.bindjob.BindingJob;


public class ProviderClassBindingJob
    extends BindingJob {

  public ProviderClassBindingJob(Scope scope, String name) {
    super(scope, null, null, name, name);
  }

}
