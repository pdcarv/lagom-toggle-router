package com.xpto.api;

import lombok.Value;

@Value
public class ToggleMessage {
    private final String id;
    private final String version;
    private final String name;
    private final Boolean enabled;
}
