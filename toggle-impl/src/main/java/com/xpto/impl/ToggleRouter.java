package com.xpto.impl;


import com.google.inject.Singleton;

import javax.inject.Inject;

@Singleton
public class ToggleRouter {
    private final ToggleConfiguration configuration;

    @Inject
    public ToggleRouter(ToggleConfiguration configuration) {
        this.configuration = configuration;
    }

    // Checks if feature is enabled
    public Boolean isFeatureEnabled() {
        return this.configuration.isEnabled();
    }

    // Checks if feature is disabled
    public Boolean isFeatureDisabled() {
        return !isFeatureEnabled();
    }
}
