package com.xpto.api.HeaderFilters;

import com.lightbend.lagom.javadsl.api.security.ServicePrincipal;
import com.lightbend.lagom.javadsl.api.security.UserAgentHeaderFilter;
import com.lightbend.lagom.javadsl.api.transport.HeaderFilter;
import com.lightbend.lagom.javadsl.api.transport.RequestHeader;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import javassist.expr.Instanceof;

import java.util.Optional;


public final class AuthenticationHeaderFilter implements HeaderFilter {

    private AuthenticationHeaderFilter() {}

    public static final HeaderFilter INSTANCE = HeaderFilter.composite(new AuthenticationHeaderFilter(), new UserAgentHeaderFilter());

    @Override
    public RequestHeader transformClientRequest(RequestHeader request) {
        return request;
    }

    @Override
    public RequestHeader transformServerRequest(RequestHeader request) {
        return request.getHeader("User-Token").map(token -> request.withPrincipal(new UserPrincipal(token))).orElse(request);
    }

    @Override
    public ResponseHeader transformServerResponse(ResponseHeader response, RequestHeader request) {
        return response;
    }

    @Override
    public ResponseHeader transformClientResponse(ResponseHeader response, RequestHeader request) {
        return response;
    }
}

