package com.xpto.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.xpto.api.ToggleService;

/**
 * The module that binds the ToggleServiceModule so that it can be served.
 */
public class ToggleServiceModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindService(ToggleService.class, ToggleServiceImpl.class);
  }
}