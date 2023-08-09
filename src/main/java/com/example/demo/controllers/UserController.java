package com.example.demo.controllers;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.UserToken;
import com.example.demo.services.impl.IAsyncTokenServiceImpl;
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
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
