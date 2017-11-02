package com.xpto.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class FeatureMessage {
    private final String id;
    private final String name;
    private final String version;
    private final String service;
    private final Boolean permission;
    private final Boolean enabled;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @JsonCreator
    public FeatureMessage(String id, String name, String version, String service, Boolean permission, Boolean enabled) {
        this.id = Preconditions.checkNotNull(id);
        this.name = name;
        this.version = Preconditions.checkNotNull(version);
        this.service = Preconditions.checkNotNull(service);
        this.permission = permission;
        this.enabled = enabled;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public FeatureMessage(String id, String version) {
        this.id = Preconditions.checkNotNull(id);
        this.version = Preconditions.checkNotNull(version);
        this.name = "";
        this.service = "default";
        this.permission = false;
        this.enabled = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
