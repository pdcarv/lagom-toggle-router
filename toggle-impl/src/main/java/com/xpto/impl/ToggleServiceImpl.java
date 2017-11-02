package com.xpto.impl;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;
import com.xpto.api.FeatureMessage;
import com.xpto.api.ToggleService;
import play.libs.F;

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
            entityRef(id, version).ask(new FeatureCommand.GetFeature()).thenApply(f -> (Optional<Feature>) f).thenApply(feature ->  {
                    if (feature.isPresent()) {
                        return ResponseFactory.toFeatureMessage(feature.get());
                    } else {
                        throw new NotFound("Feature not found");
                    }
            });

    }

    @Override
    public ServerServiceCall<NotUsed, Boolean> isEnabled(String id, String version) {
        // Returns true if for a given version/id a service is enabled
        return HeaderServiceCall.of((requestHeader, NotUsed) -> {
            Optional<String> serviceName = requestHeader.getHeader("ServiceName");

            return entityRef(id, version).ask(new FeatureCommand.GetFeature()).thenApply(f -> (Optional<Feature>) f).thenApply(feature -> {
                if (!feature.isPresent()) {
                    throw new NotFound("Feature not found");
                }

                ToggleRouter router = new ToggleRouter(new ToggleConfiguration.FeatureConfiguration(serviceName, feature.get()));
                return router.isFeatureEnabled();
            }).thenApply(isEnabled -> Pair.create(ResponseHeader.OK, isEnabled));
        });
    }

    @Override
    public ServiceCall<FeatureMessage, Done> createToggle() {
        return request ->
            entityRef(request.getId(), request.getVersion()).ask(new FeatureCommand.CreateFeature(new Feature(request.getId(), request.getName(), request.getVersion(), request.getService(), request.getPermission(), request.getEnabled())));
    }


    private PersistentEntityRef<FeatureCommand> entityRef(String id, String version) {
       return persistentEntityRegistry.refFor(FeatureEntity.class, new F.Tuple<>(id, version).toString());
    }
}
