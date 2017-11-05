package com.xpto.impl;

import com.xpto.api.FeatureMessage;
import com.xpto.api.ToggleMessage;

public class EntityMapper {
    public static Toggle toToggle(FeatureMessage msg){
       return new Toggle(msg.getId(), msg.getVersion(), msg.getName(), msg.getEnabled());
    }

    public static ToggleMessage toToggleMessage(Toggle msg){
        return new ToggleMessage(msg.getId(), msg.getVersion(), msg.getName(), msg.getEnabled());
    }

    public static ToggleMessage toToggleMessage(FeatureMessage msg){
        return new ToggleMessage(msg.getId(), msg.getVersion(), msg.getName(), msg.getEnabled());
    }

}
