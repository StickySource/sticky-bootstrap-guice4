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

import com.google.inject.Provider;

public class BeanHolder {

  @SuppressWarnings("rawtypes")
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
  @SuppressWarnings("rawtypes")
  private final class GuiceProvider
      implements Provider {

    private final javax.inject.Provider b;

    private GuiceProvider(javax.inject.Provider b) {
      this.b = b;
    }

    @Override
    public Object get() {
      return b.get();
    }
  }

  private Object instance;

  private Class<?> type;

  private String name;

  public BeanHolder(String name, Object bean, Class<?> type) {
    this.instance = bean;
    this.type = type;
    this.name = name;
  }

  public Class<?> getType() {
    return type;
  }

  public Object getInstance() {
    return instance;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return type.getSimpleName() + "@" + name;
  }

  public Provider getProvider() {
    if (instance instanceof javax.inject.Provider)
      return new GuiceProvider((javax.inject.Provider) instance);

    return new InstanceProvider(this);
  }

}