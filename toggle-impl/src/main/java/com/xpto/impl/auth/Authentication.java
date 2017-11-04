package com.xpto.impl.auth;

import com.lightbend.lagom.javadsl.api.transport.Forbidden;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;

import java.security.Principal;
import java.util.function.Function;


public class Authentication {
    public static <Request, Response> ServerServiceCall<Request, Response> authenticated(
            Function<User, ServerServiceCall<Request, Response>> serviceCall) {
        return HeaderServiceCall.composeAsync(requestHeader -> {

            String token = requestHeader.principal().map(Principal::getName).orElseThrow(() -> new Forbidden("User is not authenticated."));

            return UserServiceMock.getUser(token).thenApply(maybeUser -> {
                if (maybeUser.isPresent()) {
                    return serviceCall.apply(maybeUser.get());
                } else {
                    throw new Forbidden("User has no permission.");
                }
            });
        });
    }
}
