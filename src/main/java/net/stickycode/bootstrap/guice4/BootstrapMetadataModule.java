/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package net.stickycode.bootstrap.guice4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

public class BootstrapMetadataModule
    extends AbstractModule {

  private final class InstanceProvider
      implements Provider {

    private final BeanHolder b;

    private InstanceProvider(BeanHolder b) {
      this.b = b;
    }

    @Override
    public Object get() {
      return b.getInstance();
    }
  }

  private Logger log = LoggerFactory.getLogger(getClass());

  private final BootstrapMetadata manifest;

  public BootstrapMetadataModule(BootstrapMetadata manifest) {
    this.manifest = manifest;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  protected void configure() {
    for (final BeanHolder b : manifest.getBeans()) {
      TypeLiteral type = TypeLiteral.get(b.getType());
      log.debug("binding type '{}' to instance '{}'", type, b.getInstance());
      bind(type).toProvider(new InstanceProvider(b));
    }
    for (Class type : manifest.getTypes()) {
      log.debug("binding type '{}'", type);
      bind(type).in(Singleton.class);
      Multibinder.newSetBinder(binder(), type).addBinding().to(type);
      bindInterfaces(type, type.getInterfaces());
      bindSuperType(type, type.getSuperclass());
    }

    for (Module module : manifest.getModules()) {
      log.debug("installing module '{}'", module);
      install(module);
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void bindSuperType(Class type, Class superClass) {
    if (superClass != null && !superClass.equals(Object.class)) {
      log.debug("binding type '{}'", type);
      bind(superClass).to(type).in(Singleton.class);
      Multibinder.newSetBinder(binder(), type).addBinding().to(type);
      bindInterfaces(type, superClass.getInterfaces());
      bindSuperType(type, superClass.getSuperclass());
    }
  }

  /**
   * Recurse and bind all the interfaces implemented by the given type.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void bindInterfaces(Class type, Class[] interfaces) {
    for (Class implemented : interfaces) {
      bind(implemented).to(type).in(Singleton.class);
      bindInterfaces(type, implemented.getInterfaces());
    }
  }

}
