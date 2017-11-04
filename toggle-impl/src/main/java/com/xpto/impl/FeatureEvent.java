package com.xpto.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;


public interface FeatureEvent extends Jsonable, AggregateEvent<FeatureEvent> {

    AggregateEventTag<FeatureEvent> TAG = AggregateEventTag.of(FeatureEvent.class);

    Feature getFeature();

    @Value
    @JsonDeserialize
    final class FeatureChanged implements FeatureEvent {
        private final Feature feature;

        @JsonCreator
        public FeatureChanged(Feature feature) {
            this.feature = feature;
        }
    }

    @Override
    default AggregateEventTagger<FeatureEvent> aggregateTag() {
        return TAG;
    }
}
