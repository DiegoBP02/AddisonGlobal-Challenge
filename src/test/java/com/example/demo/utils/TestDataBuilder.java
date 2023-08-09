package com.example.demo.utils;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.entities.UserToken;

public class TestDataBuilder {
    public static Credentials buildCredentials() {
        return Credentials.builder()
                .username("secret")
                .password("SECRET")
                .build();
    }

    public static Credentials buildCredentials(String username, String password) {
        return Credentials.builder()
                .username(username)
                .password(password)
                .build();
    }

    public static User buildUserWithId() {
        Credentials credentials = buildCredentials();
        return User.builder()
                .credentials(credentials)
                .userId(credentials.getUsername())
                .id(1L)
                .build();
    }

    public static User buildUserWithId(String userId, Long id) {
        Credentials credentials = buildCredentials(userId, userId.toUpperCase());
        return User.builder()
                .credentials(credentials)
                .userId(userId)
                .id(id)
                .build();
    }

    public static User buildUserNoId() {
        Credentials credentials = buildCredentials();
        return User.builder()
                .credentials(credentials)
                .userId("secret")
                .build();
    }
    public static User buildUserNoId(String userId) {
        Credentials credentials = buildCredentials(userId, userId.toUpperCase());
        return User.builder()
                .credentials(credentials)
                .userId(userId)
                .build();
    }

    public static UserToken buildUserToken(){
        return UserToken.builder()
                .token("token")
                .build();
    }
}
