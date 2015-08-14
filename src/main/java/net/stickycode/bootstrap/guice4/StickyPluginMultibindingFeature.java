/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.RedEngine.co.nz. All rights reserved.
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

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import net.stickycode.stereotype.StickyPlugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Scope;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.util.Types;

import de.devsurf.injection.guice.install.bindjob.BindingJob;
import de.devsurf.injection.guice.install.bindjob.MultiBindingJob;

@Singleton
public class StickyPluginMultibindingFeature
    extends StickyStereotypeScannerFeature {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Override
  protected Class<? extends Annotation>[] getComponentAnnotations() {
    return new Class[] {StickyPlugin.class};
  }

  @Override
  protected <T, V extends T> void bind(Class<V> implementationClass, Class<T> interf,
      Annotation annotation, Scope scope) {
    BindingJob job = new MultiBindingJob(scope, annotation, implementationClass.getName(),
        interf.getName());

    if (!tracer.contains(job)) {
      Multibinder<T> builder;
      synchronized (_binder) {
        if (annotation != null) {
          builder = Multibinder.newSetBinder(_binder, interf, annotation);
        }
        else {
          builder = Multibinder.newSetBinder(_binder, interf);
        }

        ScopedBindingBuilder scopedBindingBuilder = builder.addBinding().to(
            implementationClass);
        if (scope != null) {
          scopedBindingBuilder.in(scope);
        }
      }
      tracer.add(job);
    }
    else {
      log.debug("Ignoring Multi-BindingJob \"" + job.toString()
          + "\", because it was already bound.", new Exception("Ignoring Multi-BindingJob \"" + job.toString()
          + "\", because it was already bound."));
      log.info("Ignoring Multi-BindingJob \"" + job.toString()
          + "\", because it was already bound.");
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void bindParameterizedType(Class<?> annotatedClass, Type type) {
    Type wildcard = Types.subtypeOf(Object.class);
    Type rawType = ((ParameterizedType)type).getRawType();
    Type target = Types.newParameterizedType(rawType, wildcard);
    TypeLiteral literal = TypeLiteral.get(target);
    Multibinder.newSetBinder(_binder, literal).addBinding().to(annotatedClass);
  }

}
