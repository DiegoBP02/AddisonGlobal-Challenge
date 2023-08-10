package com.example.demo.services;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.UserToken;

import java.util.concurrent.CompletableFuture;

public interface ISimpleAsyncTokenService {

    CompletableFuture<UserToken> issueToken(Credentials credentials);

}
