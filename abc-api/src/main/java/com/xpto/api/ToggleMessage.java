package com.xpto.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

@Value
@JsonDeserialize
public class ToggleMessage {
    private final String id;
    private final String version;
    private final String name;
    private final Boolean enabled;
}
