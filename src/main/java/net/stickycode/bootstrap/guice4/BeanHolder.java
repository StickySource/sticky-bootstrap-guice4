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

public class BeanHolder {

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

}