package com.xpto.impl;

import com.xpto.api.FeatureMessage;
import com.xpto.api.FeatureMessage;

public class EntityMapper {
    public static FeatureMessage toFeatureMessage(Feature feature) {
        return new FeatureMessage(feature.getId(), feature.getName(), feature.getVersion(), feature.getService(), feature.getServiceOnly(), feature.getEnabled());
    }

    public static Feature toFeature(FeatureMessage featureMessage){
        return new Feature(featureMessage.getId(), featureMessage.getName(), featureMessage.getVersion(), featureMessage.getService(), featureMessage.getServiceOnly(), featureMessage.getEnabled());
    }
}
