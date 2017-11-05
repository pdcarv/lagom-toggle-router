package com.xpto.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.xpto.api.AbcService;
import com.xpto.api.ToggleService;

/**
 * The module that binds the AbcServiceModule so that it can be served.
 */
public class AbcServiceModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindService(AbcService.class, AbcServiceImpl.class);
    bindClient(ToggleService.class);
  }
}
