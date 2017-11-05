package com.xpto.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

public interface AbcEvent extends Jsonable, AggregateEvent<AbcEvent> {

    AggregateEventTag<AbcEvent> TAG = AggregateEventTag.of(AbcEvent.class);

    Toggle getAbc();

    @Value
    @JsonDeserialize
    final class UpdateConfig implements AbcEvent {
        private final Toggle status;

        @JsonCreator
        public UpdateConfig(Toggle status) {
            this.status = status;
        }

        @Override
        public Toggle getAbc() {
            return this.status;
        }
    }

    @Override
    default AggregateEventTagger<AbcEvent> aggregateTag() {
        return TAG;
    }
}
