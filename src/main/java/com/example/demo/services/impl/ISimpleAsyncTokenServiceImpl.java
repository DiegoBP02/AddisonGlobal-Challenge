package com.example.demo.services.impl;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.entities.UserToken;
import com.example.demo.services.ISimpleAsyncTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ISimpleAsyncTokenServiceImpl implements ISimpleAsyncTokenService {

    private static final Logger logger = LoggerFactory.getLogger(ISimpleAsyncTokenServiceImpl.class);

    @Autowired
    private IAsyncTokenServiceImpl iAsyncTokenService;

    @Override
    public CompletableFuture<UserToken> issueToken(Credentials credentials) {
        try {
            User user = iAsyncTokenService.authenticate(credentials).get();
            return iAsyncTokenService.requestToken(user);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
