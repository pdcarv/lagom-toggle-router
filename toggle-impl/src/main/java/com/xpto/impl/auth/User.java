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

/* We are just mocking a user database, with a dummy token for concept purposes.
 * You have many options here, from google's OAUTH implementation to amazon's cognito
 * or to your own JWT or other access token based authentication.
 */
class Users {
    private static final List<User> users = Arrays.asList(
            new User("Trump", "Xm28dxc", true ),
            new User("Kim Jon Un", "da39a3e", false)
    );

    public static Optional<User> getUser(String token) {
        return users.stream().filter(u -> u.getApiToken().equals(token)).findFirst();
    }
}
