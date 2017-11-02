package com.xpto.impl;


import lombok.Builder;
import lombok.Value;

public interface ToggleConfiguration {
    public Boolean isEnabled();

    /*
    @Value
    @Builder
    final class FeatureConfiguration implements ToggleConfiguration {

        private final ToggleMessage toggleMessage;

        public FeatureConfiguration(ToggleMessage toggleMessage) {
            this.toggleMessage = toggleMessage;
        }

        @Override
        public Boolean isEnabled() {
            return this.toggleMessage.getPermission() && this.toggleMessage.getEnabled();
        }
    }*/
}
