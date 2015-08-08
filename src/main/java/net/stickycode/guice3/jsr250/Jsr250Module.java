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
package net.stickycode.guice3.jsr250;

import java.util.Map.Entry;

import net.stickycode.reflector.Reflector;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.internal.BindingImpl;
import com.google.inject.internal.Scoping;
import com.google.inject.matcher.Matchers;

public class Jsr250Module
    extends AbstractModule {

  @Override
  protected void configure() {
    bindListener(Matchers.any(), new Jsr250TypeListener());
  }

  public static void preDestroy(final Logger log, Injector injector) {
    Reflector reflector = new Reflector().forEachMethod(new PredestroyInvokingAnnotatedMethodProcessor());
    for (Entry<Key<?>, Binding<?>> binding : injector.getBindings().entrySet()) {
      Binding<?> value = binding.getValue();
      if (((BindingImpl<?>) value).getScoping().equals(Scoping.SINGLETON_ANNOTATION)) {
        reflector.process(binding.getValue().getProvider().get());
      }
      if (((BindingImpl<?>) value).getScoping().equals(Scoping.SINGLETON_INSTANCE)) {
        reflector.process(binding.getValue().getProvider().get());
      }
    }

    Injector parent = injector.getParent();
    if (parent != null)
      preDestroy(log, parent);
  }

}
