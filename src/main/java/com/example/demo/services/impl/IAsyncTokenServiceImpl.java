package com.example.demo.services.impl;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.entities.UserToken;
import com.example.demo.services.IAsyncTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class IAsyncTokenServiceImpl implements IAsyncTokenService {

    @Autowired
    private ISyncTokenServiceImpl iSyncTokenService;

    @Override
    public CompletableFuture<User> authenticate(Credentials credentials) {
        return CompletableFuture.supplyAsync(() -> iSyncTokenService.authenticate(credentials));
    }

    @Override
    public CompletableFuture<UserToken> requestToken(User user) {
        return CompletableFuture.supplyAsync(() -> iSyncTokenService.requestToken(user));
    }

}
