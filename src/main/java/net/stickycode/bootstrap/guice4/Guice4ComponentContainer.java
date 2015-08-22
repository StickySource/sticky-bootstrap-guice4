package net.stickycode.bootstrap.guice4;

import java.lang.reflect.Type;
import java.util.Set;

import javax.inject.Inject;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import net.stickycode.bootstrap.BeanNotFoundFailure;
import net.stickycode.bootstrap.ComponentContainer;

public class Guice4ComponentContainer
    implements ComponentContainer {

  @Inject
  private Injector injector;

  @Override
  public void inject(Object value) {
    injector.injectMembers(value);
  }

  @Override
  public <T> T find(Class<T> type)
      throws BeanNotFoundFailure {
    try {
      return injector.getInstance(type);
    }
    catch (ProvisionException e) {
      throw new BeanNotFoundFailure(e, type);
    }
    catch (ConfigurationException e) {
      try {
        Set<T> beans = injector.getInstance(Key.get(setOf(TypeLiteral.get(type))));
        throw new BeanNotFoundFailure(type, beans);
      }
      catch (ConfigurationException e2) {
        throw new BeanNotFoundFailure(e, type);
      }
    }
  }

  @SuppressWarnings("unchecked")
  // wrapping a T in a Set safely returns a Set<T>
  static <T> TypeLiteral<Set<T>> setOf(TypeLiteral<T> elementType) {
    Type type = Types.setOf(elementType.getType());
    return (TypeLiteral<Set<T>>) TypeLiteral.get(type);
  }
}
