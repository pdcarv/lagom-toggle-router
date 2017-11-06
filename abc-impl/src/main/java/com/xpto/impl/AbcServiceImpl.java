package com.xpto.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.Offset;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.xpto.api.AbcService;
import com.xpto.api.FeatureMessage;
import com.xpto.api.ToggleMessage;
import com.xpto.api.ToggleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;


/**
 * Implementation of the AbcService.
 */
public class AbcServiceImpl implements AbcService {
    private PersistentEntityRegistry registry;
    private ToggleService toggleService;
    private final Logger log = LoggerFactory.getLogger(AbcServiceImpl.class);

    @Inject
    public AbcServiceImpl(ToggleService toggleService, PersistentEntityRegistry registry) {
        this.registry = registry;
        this.toggleService = toggleService;

        this.registry.register(AbcEntity.class);

        this.toggleService.featureChanged().subscribe().atLeastOnce(Flow.fromFunction((FeatureMessage message) -> {
            // If it is destined to this service, consume
            if (message.getService().equals(Constants.SERVICE_NAME)) {
                log.warn(String.format("AbcService: received message %s, persisting configuration.", message));

                Toggle toggle = EntityMapper.toToggle(message);

                entityRef(toggle.getId()).ask(new AbcCommand.UpdateConfig(toggle));
            }

            return Done.getInstance();
        }));
    }

    @Override
    public ServiceCall<NotUsed, Source<ToggleMessage, NotUsed>> toggleStream() {
        return request -> {
            return completedFuture(registry.eventStream(AbcEvent.TAG, Offset.NONE).map(f ->
                    EntityMapper.toToggleMessage(f.first().getAbc())
            ));
        };
    }

    private PersistentEntityRef<AbcCommand> entityRef(String id) {
        return registry.refFor(AbcEntity.class, id);
    }
}
