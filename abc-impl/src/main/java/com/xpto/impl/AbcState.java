package com.xpto.impl;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AbcState {
    private final Boolean updated;

    @JsonCreator
    public AbcState(Boolean updated) {
        this.updated = updated;
    }
}
