package com.example.demo.services;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.entities.UserToken;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface IAsyncTokenService {
    CompletableFuture<User> authenticate(Credentials credentials);

    CompletableFuture<UserToken> requestToken(User user);

    default Future<UserToken> issueToken(Credentials credentials) {
        return authenticate(credentials).thenCompose(this::requestToken);
    }

}