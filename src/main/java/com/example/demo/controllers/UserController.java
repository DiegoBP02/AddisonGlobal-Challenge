package com.example.demo.controllers;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.entities.UserToken;
import com.example.demo.services.impl.ISyncTokenServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private ISyncTokenServiceImpl iSyncTokenService;

    @PostMapping("/issueToken")
    public ResponseEntity<UserToken> issueToken(@Valid @RequestBody Credentials credentials) {
        UserToken user = iSyncTokenService.issueToken(credentials);
        return ResponseEntity.ok(user);
    }

}
