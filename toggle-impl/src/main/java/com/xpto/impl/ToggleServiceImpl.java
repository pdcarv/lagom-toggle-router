package com.xpto.impl;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.transport.Forbidden;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;
import com.xpto.api.FeatureMessage;
import com.xpto.api.ToggleService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import play.libs.F;

import javax.inject.Inject;
import java.util.Optional;

import static com.xpto.impl.auth.Authentication.authenticated;


/**
 * Implementation of the ToggleService.
 */
public class ToggleServiceImpl implements ToggleService {

    private final Logger log = LoggerFactory.getLogger(ToggleServiceImpl.class);
    private final PersistentEntityRegistry persistentEntityRegistry;
    private final ToggleRouter.Factory toggleRouterFactory;

    @Inject
    public ToggleServiceImpl(PersistentEntityRegistry persistentEntityRegistry, ToggleRouter.Factory toggleRouterFactory) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.toggleRouterFactory = toggleRouterFactory;
        persistentEntityRegistry.register(FeatureEntity.class);
    }


    @Override
    public ServiceCall<NotUsed, FeatureMessage> toggle(String id, String version) {
        return request ->
                entityRef(id, version).ask(new FeatureCommand.GetFeature()).thenApply(f -> (Optional<Feature>) f).thenApply(feature ->  {
                    if (!feature.isPresent()) {
                        throw new NotFound("Feature not found");
                    }

                    return EntityMapper.toFeatureMessage(feature.get());
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

                return this.toggleRouterFactory.buildInstance(serviceName, feature.get()).isEnabled();
            }).thenApply(isEnabled -> Pair.create(ResponseHeader.OK, isEnabled));
        });
    }

    @Override
    public Topic<FeatureMessage> featureChanged() {
        return TopicProducer.singleStreamWithOffset(offset -> {
            return persistentEntityRegistry.eventStream(FeatureEventTag.INSTANCE, offset).map(f -> {
                Feature feature = f.first().getFeature();
                return new Pair<>(EntityMapper.toFeatureMessage(feature), f.second());
            });
        });
    }

    @Override
    public ServiceCall<FeatureMessage, Done> createToggle() {
        return authenticated( user -> request -> {
            if (!user.getIsAdmin()) {
                throw new Forbidden("You must have admin privileges");
            }

            return entityRef(request.getId(), request.getVersion()).ask(new FeatureCommand.CreateFeature(EntityMapper.toFeature(request)));
        });
    }


    private PersistentEntityRef<FeatureCommand> entityRef(String id, String version) {
       return persistentEntityRegistry.refFor(FeatureEntity.class, new F.Tuple<>(id, version).toString());
    }
}
