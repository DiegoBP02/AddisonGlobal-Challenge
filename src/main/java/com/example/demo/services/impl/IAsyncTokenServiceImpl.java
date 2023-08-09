package com.example.demo.services.impl;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.entities.UserToken;
import com.example.demo.exceptions.DelayInterruptedException;
import com.example.demo.exceptions.InvalidCredentialsException;
import com.example.demo.exceptions.InvalidUserIdException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.IAsyncTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class IAsyncTokenServiceImpl implements IAsyncTokenService {

    private static final Logger logger = LoggerFactory.getLogger(IAsyncTokenServiceImpl.class);

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
