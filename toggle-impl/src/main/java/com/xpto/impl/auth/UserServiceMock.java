package com.xpto.impl.auth;


import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;


interface UserServiceMock {
    static CompletableFuture<Optional<User>> getUser(String username) {
        return completedFuture(Users.getUser(username));
    }
}
