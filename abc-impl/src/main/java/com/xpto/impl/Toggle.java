package com.xpto.impl;

import lombok.Value;

@Value
public class Toggle {
    private final String id;
    private final String version;
    private final String name;
    private final Boolean enabled;
}
