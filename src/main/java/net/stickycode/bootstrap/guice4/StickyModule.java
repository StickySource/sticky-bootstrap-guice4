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
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.spi.TypeListener;
import com.google.inject.util.Types;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.stickycode.bootstrap.ComponentContainer;
import net.stickycode.reflector.Methods;
import net.stickycode.stereotype.StickyComponent;
import net.stickycode.stereotype.StickyDomain;
import net.stickycode.stereotype.StickyPlugin;

public class StickyModule
    extends AbstractModule {

  private Logger log = LoggerFactory.getLogger(getClass());

  private static boolean tellMeWhatsGoingOn;

  static {
    java.util.logging.Logger util = LogManager.getLogManager().getLogger("");
    for (java.util.logging.Handler handler : util.getHandlers())
      util.removeHandler(handler);
    SLF4JBridgeHandler.install();

    tellMeWhatsGoingOn = new Boolean(System.getProperty("sticky.bootstrap.debug"));
    if (!tellMeWhatsGoingOn)
      LoggerFactory.getLogger(StickyModule.class).debug("Enable binding trace with -Dsticky.bootstrap.debug=true");
  }

  private FastClasspathScanner scanner;

  public StickyModule(FastClasspathScanner scanner) {
    this.scanner = scanner;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void configure() {

    binder().requireExplicitBindings();
    binder().bind(ComponentContainer.class).to(Guice4ComponentContainer.class);

    for (String name : getComponentNames()) {
      @SuppressWarnings("rawtypes")
      Class k = scanner.loadClass(name);
      bind(k, scanner);
    }
  }

  List<String> getComponentNames() {
    return scanner.getNamesOfClassesWithMetaAnnotation(
        StickyComponent.class,
        StickyPlugin.class,
        StickyDomain.class);
  }

  @SuppressWarnings({ "unchecked" })
  private void bind(Class<Object> annotatedClass, FastClasspathScanner scanner) {

    List<Class<?>> interfaces = collectInterfaces(annotatedClass);

    Scope scope = deriveScope(annotatedClass, interfaces);

    debug("bind {}", annotatedClass);
    binder().bind(annotatedClass).in(scope);

    for (Class<?> interf : interfaces) {

      if (interf.isAssignableFrom(TypeListener.class))
        bindListener(annotatedClass);
      else
        if (Provider.class.isAssignableFrom(interf))
          bindProviderWorkaround((Class<Object>) annotatedClass, Scopes.NO_SCOPE);
        else
          bind(annotatedClass, (Class<Object>) interf, (Annotation) null, scope);
    }

    if (!Provider.class.isAssignableFrom(annotatedClass) && !MembersInjector.class.isAssignableFrom(annotatedClass))
      for (Class<?> blah = annotatedClass; blah != null; blah = blah.getSuperclass())
        for (Type type : blah.getGenericInterfaces()) {
          if (type instanceof ParameterizedType) {
            bindParameterizedType(annotatedClass, type);
          }
        }
  }

  private void debug(String message, Object... paraemeters) {
    if (tellMeWhatsGoingOn)
      log.debug(message, paraemeters);
  }

  private <T, V extends T> void bind(Class<V> implementationClass, Class<T> contract,
      Annotation annotation, Scope scope) {

    if (MembersInjector.class.isAssignableFrom(contract)) {
      debug("ignore MembersInjector interfaces on {} its internal to guice", implementationClass);
      return;
    }

    List<String> implementors = scanner.getNamesOfClassesImplementing(contract);
    implementors.removeAll(scanner.getNamesOfSuperclassesOf(implementationClass));
    if (implementors.size() == 1) {
      debug("bind {} to {}", implementationClass, contract);
      LinkedBindingBuilder<T> builder = binder().bind(contract);
      if (annotation != null) {
        builder = ((AnnotatedBindingBuilder<T>) builder).annotatedWith(annotation);
      }
      ScopedBindingBuilder scopedBindingBuilder = builder.to(implementationClass);
      if (scope != null) {
        scopedBindingBuilder.in(scope);
      }
    }
    else {
      debug("only multi binding {} to {} due to many implementations {}", implementationClass, contract, implementors);
    }
    debug("multibind {} to {}", implementationClass, contract);
    Multibinder.newSetBinder(binder(), contract).addBinding().to(implementationClass);
  }

  /**
   * This nasty code is to workaround the bug fixed by (NOTE its says closed but its not fixed yet) in javac. Without these casts
   * javac will fail while ecj will be fine.
   */
  @SuppressWarnings("unchecked")
  private void bindProviderWorkaround(Class<Object> annotatedClass, Scope scope) {
    Class<Object> annotatedClass2 = annotatedClass;
    if (annotatedClass2 instanceof Class)
      bindProvider((Class<? extends Provider<Object>>) (Object) annotatedClass2, scope);
  }

  @SuppressWarnings("unchecked")
  private <Y, T extends Provider<Y>> void bindProvider(Class<T> providerClass, Scope scope) {
    Method m = Methods.find(providerClass, "get");
    // ParameterizedType t = Types.providerOf();
    TypeLiteral<T> tl = (TypeLiteral<T>) TypeLiteral.get(providerClass);
    debug("bind {} to provider {}", m.getReturnType(), tl);
    binder().bind((Class<Y>) m.getReturnType())
        .toProvider(tl).in(scope);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void bindParameterizedType(Class<?> annotatedClass, Type type) {
    Type wildcard = Types.subtypeOf(Object.class);
    Type rawType = ((ParameterizedType) type).getRawType();
    Type target = Types.newParameterizedType(rawType, wildcard);
    TypeLiteral literal = TypeLiteral.get(target);
    debug("multi bind paramterized type {} to {}", literal, annotatedClass);
    Multibinder.newSetBinder(binder(), literal).addBinding().to(annotatedClass);
  }

  private void bindListener(Class<Object> annotatedClass) {
    TypeListener typeListener = typeListener(annotatedClass);
    binder().requestInjection(typeListener);
    debug("bind {} as type listener", typeListener);
    binder().bindListener(Matchers.any(), typeListener);
  }

  private TypeListener typeListener(Class<Object> annotatedClass) {
    try {
      return (TypeListener) annotatedClass.newInstance();
    }
    catch (InstantiationException e) {
      throw new RuntimeException(e);
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private Scope deriveScope(Class<Object> annotatedClass, List<Class<?>> interfaces) {
    if (scanner.getNamesOfClassesWithMetaAnnotation(StickyDomain.class).contains(annotatedClass.getName()))
      return Scopes.NO_SCOPE;

    return Scopes.SINGLETON;
  }

  private List<Class<?>> collectInterfaces(Class<Object> annotatedClass) {
    List<Class<?>> interfaces = new ArrayList<Class<?>>();
    for (Class<?> base = annotatedClass; base != null; base = base.getSuperclass())
      for (Class<?> class1 : base.getInterfaces()) {
        interfaces.add(class1);
        processInterface(class1, interfaces);
      }

    debug("found {} with {}", annotatedClass, interfaces);
    return interfaces;
  }

  private void processInterface(Class<?> target, List<Class<?>> interfaces) {
    for (Class<?> class1 : target.getInterfaces()) {
      interfaces.add(class1);
      processInterface(class1, interfaces);
    }
  }
}
