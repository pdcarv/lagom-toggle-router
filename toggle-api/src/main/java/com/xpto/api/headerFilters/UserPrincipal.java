package com.xpto.api.headerFilters;

import javax.security.auth.Subject;
import java.security.Principal;

public class UserPrincipal implements Principal {
    private final String token;

    public UserPrincipal(String token) {
        this.token = token;
    }

    @Override
    public String getName() {
        return this.token;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
