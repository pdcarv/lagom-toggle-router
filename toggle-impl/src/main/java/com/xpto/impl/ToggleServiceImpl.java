package com.xpto.impl;

import akka.Done;
import akka.NotUsed;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.xpto.api.FeatureMessage;
import com.xpto.api.ToggleService;

import java.util.Optional;


/**
 * Implementation of the ToggleService.
 */
public class ToggleServiceImpl implements ToggleService {

    private final PersistentEntityRegistry persistentEntityRegistry;

    @Inject
    public ToggleServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        persistentEntityRegistry.register(FeatureEntity.class);
    }


    @Override
    public ServiceCall<NotUsed, FeatureMessage> toggle(String id, String version) {
        return request ->
            entityRef(id).ask(new FeatureCommand.GetFeature()).thenApply(f -> (Optional<Feature>) f).thenApply(feature ->
                    ResponseFactory.toFeatureMessage(feature.get())
            );

    }

    @Override
    public ServiceCall<FeatureMessage, Done> createToggle() {
        return request ->
            entityRef(request.getId()).ask(new FeatureCommand.CreateFeature(new Feature(request.getId(), request.getName(), request.getVersion(), request.getService(), request.getPermission(), request.getEnabled())));
    }

    private PersistentEntityRef<FeatureCommand> entityRef(String id) {
       return persistentEntityRegistry.refFor(FeatureEntity.class, id);
    }
}
