package com.xpto.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

import java.util.Optional;

@SuppressWarnings("serial")
@Value
@JsonDeserialize
public final class FeatureState implements CompressedJsonable {
    private final Optional<Feature> feature;

    @JsonCreator
    public FeatureState(Optional<Feature> feature) {
        this.feature = feature;
    }
}
