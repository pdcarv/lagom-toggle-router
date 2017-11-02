package com.xpto.impl;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

import java.util.Optional;


public interface FeatureCommand extends Jsonable {

    @Value
    @JsonDeserialize
    final class GetFeature implements FeatureCommand, CompressedJsonable, PersistentEntity.ReplyType<Optional<Feature>> {
    }

    @Value
    @JsonDeserialize
    final class CreateFeature implements FeatureCommand, CompressedJsonable, PersistentEntity.ReplyType<Done>{
        private final Feature feature;

        @JsonCreator
        public CreateFeature(Feature feature) {
            this.feature = feature;
        }
    }
}
