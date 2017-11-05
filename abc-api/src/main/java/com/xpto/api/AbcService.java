

package com.xpto.api;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface AbcService extends Service {

    ServiceCall<NotUsed, Source<ToggleMessage, NotUsed>> toggleStream();

    @Override
    default Descriptor descriptor() {
        return named("abc").withCalls(
                pathCall("/api/v1/toggle/stream", this::toggleStream)
        ).withAutoAcl(true);
    }
}
        