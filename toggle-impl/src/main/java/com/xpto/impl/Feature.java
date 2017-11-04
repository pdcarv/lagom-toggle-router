package com.xpto.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

@Value
@JsonDeserialize
public class Feature implements Jsonable {
    private final String id;
    private final String name;
    private final String version;
    private final String service;
    private final Boolean serviceOnly;
    private final Boolean enabled;

    @JsonCreator
    @Inject
    public Feature(String id, String name, String version, String service, Boolean serviceOnly, Boolean enabled) {
        this.id = Preconditions.checkNotNull(id);
        this.name = name;
        this.version = Preconditions.checkNotNull(version);
        this.service = Preconditions.checkNotNull(service);
        this.serviceOnly = serviceOnly;
        this.enabled = enabled;
    }

    public Feature(String id, String version) {
        this.id = Preconditions.checkNotNull(id);
        this.version = Preconditions.checkNotNull(version);
        this.name = "";
        this.service = "default";
        this.serviceOnly = false;
        this.enabled = false;
    }
}
