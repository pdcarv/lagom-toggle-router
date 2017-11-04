package com.xpto.impl.auth;

import lombok.Value;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Value
public class User {
    private final String username;
    private final String apiToken;
    private final Boolean isAdmin;
}

class Users {
    private static final List<User> users = Arrays.asList(
            new User("Trump", "Xm28dxc", true )
    );

    public static Optional<User> getUser(String token) {
        return users.stream().filter(u -> u.getApiToken().equals(token)).findFirst();
    }
}
