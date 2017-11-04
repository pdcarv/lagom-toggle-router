package com.xpto.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Value;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FeatureChangedEvent.FeatureChangedChanged.class, name = "feature-changed")
})
public interface FeatureChangedEvent {

    String getName();

    @Value
    final class FeatureChangedChanged implements FeatureChangedEvent {
        private final FeatureMessage featureMessage;


        @JsonCreator
        public FeatureChangedChanged(FeatureMessage featureMessage) {
            this.featureMessage= featureMessage;
        }

        @Override
        public String getName() {
            return null;
        }
    }
}
