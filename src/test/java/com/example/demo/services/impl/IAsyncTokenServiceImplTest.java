package com.example.demo.services.impl;

import com.example.demo.ApplicationConfigTest;
import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.entities.UserToken;
import com.example.demo.exceptions.InvalidCredentialsException;
import com.example.demo.exceptions.InvalidUserIdException;
import com.example.demo.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IAsyncTokenServiceImplTest extends ApplicationConfigTest {

    @Autowired
    private IAsyncTokenServiceImpl iAsyncTokenService;

    @MockBean
    private ISyncTokenServiceImpl iSyncTokenService;

    User user = TestDataBuilder.buildUserWithId();
    Credentials credentials = TestDataBuilder.buildCredentials();
    UserToken userToken = TestDataBuilder.buildUserToken();

    @Test
    void givenValidCredentialsAndUser_whenAuthenticate_thenReturnUser() throws ExecutionException, InterruptedException {
        when(iSyncTokenService.authenticate(credentials))
                .thenReturn(user);

        CompletableFuture<User> resultFuture = iAsyncTokenService.authenticate(credentials);
        User result = resultFuture.get();

        assertThat(result).isEqualTo(user);

        verify(iSyncTokenService, times(1)).authenticate(credentials);
    }

    @Test
    void givenInvalidCredentials_whenAuthenticate_thenThrowInvalidCredentialsException() {
        when(iSyncTokenService.authenticate(credentials))
                .thenThrow(InvalidCredentialsException.class);

        CompletableFuture <User> resultFuture = iAsyncTokenService.authenticate(credentials);

        assertThrows(ExecutionException.class, () -> resultFuture.get());

        verify(iSyncTokenService, times(1)).authenticate(credentials);
    }

    @Test
    void givenValidUser_whenRequestToken_thenReturnUserToken() throws ExecutionException, InterruptedException {
        when(iSyncTokenService.requestToken(user))
                .thenReturn(userToken);

        CompletableFuture<UserToken> resultFuture = iAsyncTokenService.requestToken(user);
        UserToken result = resultFuture.get();

        assertThat(result.getToken()).isEqualTo(userToken.getToken());

        verify(iSyncTokenService, times(1)).requestToken(user);
    }

    @Test
    void givenValidUser_whenRequestToken_thenThrowInvalidUserIdException() {
        when(iSyncTokenService.requestToken(user))
                .thenThrow(InvalidUserIdException.class);

        CompletableFuture<UserToken> resultFuture = iAsyncTokenService.requestToken(user);

        assertThrows(ExecutionException.class, () -> resultFuture.get());

        verify(iSyncTokenService, times(1)).requestToken(user);
    }

}