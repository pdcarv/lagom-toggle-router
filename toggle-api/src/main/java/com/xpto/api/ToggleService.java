package com.xpto.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface ToggleService extends Service {

    /**
     * Example: curl http://localhost:9000/api/v1/toggle/:id/version/:version
     * Retrieves one toggle
     */
    ServiceCall<NotUsed, FeatureMessage> toggle(String id, String version);

    /**
     * Create one toggle
     * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
     * "Hi"}' http://localhost:9000/api/hello/Alice
     *
     */
    ServiceCall<FeatureMessage, Done> createToggle();

    /**
     * This gets published to Kafka.
     */
    //Topic<ToggleEvent> toggleConfigurationChanged();

    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("toggle").withCalls(
                pathCall("/api/v1/toggle/:id/version/:version",  this::toggle),
                pathCall("/api/v1/toggle", this::createToggle)
        ).withAutoAcl(true);
        // @formatter:on
    }
}
