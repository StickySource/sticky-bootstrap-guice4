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

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import net.stickycode.stereotype.StickyComponent;
import net.stickycode.stereotype.StickyFramework;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

@StickyComponent
@StickyFramework
public class Jsr250TypeListener
    implements TypeListener {

  @Override
  public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
    for (Method method : type.getRawType().getDeclaredMethods())
      if (method.isAnnotationPresent(PostConstruct.class))
        encounter.register(new PostConstructInjectionListener());
  }

}
