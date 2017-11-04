package com.xpto.impl;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

public interface RouterConfiguration {
    public Boolean isEnabled();
}
