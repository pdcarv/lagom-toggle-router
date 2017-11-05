package com.xpto.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.xpto.api.ToggleService;
import com.xpto.impl.toggleRouter.FeatureConfiguration;
import com.xpto.impl.toggleRouter.Router;
import com.xpto.impl.toggleRouter.RouterConfiguration;
import com.xpto.impl.toggleRouter.ToggleRouter;

/**
 * The module that binds the ToggleServiceModule so that it can be served.
 */
public class ToggleServiceModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindService(ToggleService.class, ToggleServiceImpl.class);
    bind(Router.class).to(ToggleRouter.class);
    bind(ToggleRouter.Factory.class).to(ToggleRouter.Factory.ToggleRouterFactory.class);
    bind(RouterConfiguration.class).to(FeatureConfiguration.class);
  }
}