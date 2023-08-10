package com.example.demo.services;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.entities.UserToken;

public interface ISyncTokenService {
    User authenticate(Credentials credentials);

    UserToken requestToken(User user);

    default UserToken issueToken(Credentials credentials) {
        User user = authenticate(credentials);
        return requestToken(user);
    }

}
