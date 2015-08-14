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

import static net.stickycode.bootstrap.guice4.StickyModule.bootstrapModule;
import static net.stickycode.bootstrap.guice4.StickyModule.keyBuilderModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.devsurf.injection.guice.scanner.PackageFilter;

public class StickyGuice {

  static public Injector createInjector(String... packages) {
    PackageFilter[] packageFilters = createFilters(packages);
    return Guice.createInjector(
        bootstrapModule(packageFilters),
        keyBuilderModule());

  }

  static public Injector createApplicationInjector(String... packages) {
    PackageFilter[] packageFilters = createFilters(packages);
    return Guice.createInjector(
        bootstrapModule(packageFilters),
        keyBuilderModule())
        .createChildInjector(StickyModule.applicationModule(packageFilters));

  }

  private static PackageFilter[] createFilters(String[] packages) {
    PackageFilter[] filters = new PackageFilter[packages.length + 1];
    filters[0] = PackageFilter.create("net.stickycode");
    for (int i = 1; i < filters.length; i++) {
      filters[i] = PackageFilter.create(packages[i - 1]);
    }
    return filters;
  }

  public static Injector createInjector(Injector injector, String... packages) {
    return injector
        .createChildInjector(StickyModule.applicationModule(createFilters(packages)));
  }

}
