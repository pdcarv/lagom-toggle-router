package com.xpto.impl.toggleRouter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.xpto.impl.Feature;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;


@Value
@Builder
public final class FeatureConfiguration implements RouterConfiguration {
    private final Optional<String> service;
    private final Feature data;

    public FeatureConfiguration() {
        this.service = Optional.empty();
        this.data = null;
    }

    @JsonCreator
    public FeatureConfiguration(Optional<String> service, Feature data) {
        this.service = Optional.of(service.orElse(""));
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
        if (data.getServiceOnly()) {
            return service.get().equals(data.getService()) && data.getEnabled();
        }

        return !service.get().equals(data.getService()) && !data.getEnabled();
    }
}

