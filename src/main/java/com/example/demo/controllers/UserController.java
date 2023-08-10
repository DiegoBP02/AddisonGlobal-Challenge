package com.example.demo.controllers;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.UserToken;
import com.example.demo.exceptions.DelayInterruptedException;
import com.example.demo.exceptions.InvalidCredentialsException;
import com.example.demo.exceptions.InvalidUserIdException;
import com.example.demo.services.impl.IAsyncTokenServiceImpl;
import com.example.demo.services.impl.ISimpleAsyncTokenServiceImpl;
import com.example.demo.services.impl.ISyncTokenServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/auth")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ISyncTokenServiceImpl iSyncTokenService;

    @Autowired
    private IAsyncTokenServiceImpl iAsyncTokenService;

    @Autowired
    private ISimpleAsyncTokenServiceImpl iSimpleAsyncTokenService;

    @PostMapping("/issueToken")
    public ResponseEntity<UserToken> issueToken(@Valid @RequestBody Credentials credentials) {
        UserToken user = iSyncTokenService.issueToken(credentials);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/issueTokenAsync")
    public ResponseEntity<UserToken> issueTokenAsync(@Valid @RequestBody Credentials credentials) {
        Future<UserToken> userTokenFuture = iAsyncTokenService.issueToken(credentials);
        try {
            return ResponseEntity.ok(userTokenFuture.get());
        } catch (Throwable t) {
            return handleException(t);
        }
    }

    @PostMapping("/simpleIssueTokenAsync")
    public ResponseEntity<UserToken> simpleIssueTokenAsync(@Valid @RequestBody Credentials credentials) {
        CompletableFuture<UserToken> userTokenFuture = iSimpleAsyncTokenService.issueToken(credentials);
        try {
            return ResponseEntity.ok(userTokenFuture.get());
        } catch (Throwable t) {
            return handleException(t);
        }
    }

    private ResponseEntity<UserToken> handleException(Throwable t) {
        logger.error(t.getMessage());
        if (t instanceof ExecutionException) {
            t = t.getCause();
        }
        if (t instanceof InvalidCredentialsException) {
            throw (InvalidCredentialsException) t;
        } else if (t instanceof InvalidUserIdException) {
            throw (InvalidUserIdException) t;
        } else if (t instanceof DelayInterruptedException) {
            throw (DelayInterruptedException) t;
        } else {
            throw new RuntimeException(t);
        }
    }

}
