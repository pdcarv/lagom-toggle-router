package com.xpto.impl;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

public interface ToggleConfiguration {
    public Boolean isEnabled();

    @Value
    @Builder
    final class FeatureConfiguration implements ToggleConfiguration {
        private final Optional<String> service;
        private final Feature data;

        @JsonCreator
        public FeatureConfiguration(Optional<String> service, Feature data) {
            this.service = Optional.ofNullable(service.orElse(""));
            this.data = data;
        }

        @Override
        public Boolean isEnabled() {
            /* If no service is configurated for this toggle we discard permission and
             * check only if the toggle is enabled
             */
            if (isBlank(data.getService())) {
               return data.getEnabled();
            }

            /*
             * If permission is false, it means this isn't a service exclusive configuration
             */
            if (!data.getPermission()) {
                return !service.get().equals(data.getService()) && !data.getEnabled();
            }

            return service.get().equals(data.getService()) && data.getEnabled();
        }
    }
}
