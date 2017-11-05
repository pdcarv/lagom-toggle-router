package com.xpto.impl;

import com.xpto.api.FeatureMessage;
import com.xpto.api.FeatureMessage;

public class ResponseMapper {
    public static FeatureMessage toFeatureMessage(Feature feature) {
        return new FeatureMessage(feature.getId(), feature.getName(), feature.getVersion(), feature.getService(), feature.getServiceOnly(), feature.getEnabled());
    }
}
