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

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Module;

/**
 * Manifest of the defined types and beans that will be used to construct the test module.
 */
public class BootstrapMetadata {

  private List<Class<?>> types = new ArrayList<Class<?>>();
  private List<BeanHolder> beans = new ArrayList<BeanHolder>();
  private List<Module> modules = new ArrayList<Module>();

  public boolean hasRegisteredType(Class<?> type) {
    for (Class<?> t : types)
      if (t.isAssignableFrom(type))
        return true;

    for (BeanHolder b : beans)
      if (b.getType().isAssignableFrom(type))
        return true;

    return false;
  }

  public void registerBean(String beanName, Object bean, Class<?> type) {
    beans.add(new BeanHolder(beanName, bean, type));
  }

  public void registerType(String beanName, Class<?> type) {
    types.add(type);
  }

  public List<Class<?>> getTypes() {
    return types;
  }

  public List<BeanHolder> getBeans() {
    return beans;
  }

  @SuppressWarnings("unchecked")
  public <T> T getBean(Class<T> type) {
    for (BeanHolder b : beans) {
      if (b.getType().equals(type))
        return (T) b.getInstance();
    }

    return null;
  }

  public void registerModule(Module module) {
    modules.add(module);
  }

  List<Module> getModules() {
    return modules;
  }

}