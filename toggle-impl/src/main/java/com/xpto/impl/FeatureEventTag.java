package com.xpto.impl;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

public class FeatureEventTag {
    public static final AggregateEventTag<FeatureEvent> INSTANCE =
            AggregateEventTag.of(FeatureEvent.class);
}
