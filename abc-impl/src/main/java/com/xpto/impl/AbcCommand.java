package com.xpto.impl;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

public interface AbcCommand extends Jsonable {

    @Value
    @JsonDeserialize
    final class UpdateConfig implements AbcCommand, CompressedJsonable, PersistentEntity.ReplyType<Done>{
        private final Toggle toggle;

        @JsonCreator
        public UpdateConfig(Toggle toggle) {
           this.toggle = toggle;
        }
    }
}
