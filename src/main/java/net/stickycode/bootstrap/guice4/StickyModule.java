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

import java.util.logging.LogManager;

import net.stickycode.metadata.MetadataResolverRegistry;
import net.stickycode.metadata.ReflectiveMetadataResolverRegistry;

import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import de.devsurf.injection.guice.scanner.ClasspathScanner;
import de.devsurf.injection.guice.scanner.PackageFilter;
import de.devsurf.injection.guice.scanner.StartupModule;
import de.devsurf.injection.guice.scanner.features.ScannerFeature;

public class StickyModule extends StartupModule {
  static {
    java.util.logging.Logger util = LogManager.getLogManager().getLogger("");
    for (java.util.logging.Handler handler : util.getHandlers())
      util.removeHandler(handler);
    SLF4JBridgeHandler.install();
  }
  
  static public Module bootstrapModule(PackageFilter... packageFilter) {
    return new StickyModule(StickyClasspathScanner.class, packageFilter)
        .addFeature(StickyFrameworkPluginMultibindingFeature.class)
        .addFeature(StickyFrameworkStereotypeScannerFeature.class)
        .disableStartupConfiguration();
  }

  static public Module applicationModule(PackageFilter... packageFilter) {
    return 
        new StickyModule(StickyClasspathScanner.class, packageFilter)
        .addFeature(StickyPluginMultibindingFeature.class)
        .addFeature(StickyStereotypeScannerFeature.class)
        .disableStartupConfiguration();
  }

  static public Module keyBuilderModule() {
    return new AbstractModule() {

      @Override
      protected void configure() {
        binder().requireExplicitBindings();
      }
    };
  }

  public StickyModule(Class<? extends ClasspathScanner> scanner, PackageFilter... filter) {
    super(scanner, filter);
  }

  @Override
  protected Multibinder<ScannerFeature> bindFeatures(Binder binder) {
    Multibinder<ScannerFeature> listeners = Multibinder.newSetBinder(binder,
      ScannerFeature.class);

    for (Class<? extends ScannerFeature> listener : _features) {
      listeners.addBinding().to(listener);
    }
    
    binder.bind(MetadataResolverRegistry.class).to(ReflectiveMetadataResolverRegistry.class);

    return listeners;
  }

}
