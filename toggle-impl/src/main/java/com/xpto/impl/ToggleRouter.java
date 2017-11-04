package com.xpto.impl;


import com.google.inject.Singleton;

import javax.inject.Inject;
import java.util.Optional;


@Singleton
public class ToggleRouter implements Router {
    private final RouterConfiguration configuration;

    @Inject
    public ToggleRouter(RouterConfiguration configuration) {
        this.configuration = configuration;
    }

    // Checks if feature is enabled
    public Boolean isEnabled() {
        return this.configuration.isEnabled();
    }

    // Checks if feature is disabled
    public Boolean isDisabled() {
        return !isEnabled();
    }

    public interface Factory {
        Router buildInstance(Optional<String> serviceName, Feature feature);

        public class ToggleRouterFactory implements Factory {
            public ToggleRouter buildInstance(Optional<String> serviceName, Feature feature) {
                return new ToggleRouter(new FeatureConfiguration(serviceName, feature));
            }
        }
    }
}
