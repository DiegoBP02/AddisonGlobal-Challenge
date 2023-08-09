package com.example.demo.services.impl;

import com.example.demo.ApplicationConfigTest;
import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.entities.UserToken;
import com.example.demo.exceptions.InvalidCredentialsException;
import com.example.demo.exceptions.InvalidUserIdException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ISyncTokenServiceImplTest extends ApplicationConfigTest {

    @Autowired
    private ISyncTokenServiceImpl iSyncTokenService;

    @MockBean
    private UserRepository userRepository;

    User user = TestDataBuilder.buildUserWithId();
    Credentials credentials = TestDataBuilder.buildCredentials();

    @Test
    void givenValidCredentialsAndUser_whenAuthenticate_thenReturnUser() {
        String username = credentials.getUsername();
        when(userRepository.findByCredentialsUsername(username))
                .thenReturn(user);

        User result = iSyncTokenService.authenticate(credentials);
        assertEquals(user, result);

        verify(userRepository, times(1)).findByCredentialsUsername(username);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenValidCredentialsAndNoUser_whenAuthenticate_thenCreateAndReturnUser() {
        String username = credentials.getUsername();
        when(userRepository.findByCredentialsUsername(username))
                .thenReturn(null);

        User result = iSyncTokenService.authenticate(credentials);
        assertThat(result).isInstanceOf(User.class);

        verify(userRepository, times(1)).findByCredentialsUsername(username);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void givenInvalidCredentials_whenAuthenticate_thenThrowInvalidCredentialsException() {
        credentials.setPassword("random");

        assertThrows(InvalidCredentialsException.class, () ->
                iSyncTokenService.authenticate(credentials));

        verify(userRepository, never()).findByCredentialsUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenValidUserId_whenRequestToken_thenReturnUserToken() {
        UserToken result = iSyncTokenService.requestToken(user);
        assertThat(result).isInstanceOf(UserToken.class);
        assertTrue(result.getToken().startsWith(user.getUserId()));
        assertThat(result.getToken().substring(user.getUserId().length() + 1))
                .matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"); // "yyyy-MM-dd'T'HH:mm:ssX"
    }

    @Test
    void givenInvalidUserId_whenRequestToken_thenThrowInvalidUserIdException() {
        user.setUserId("A");
        assertThrows(InvalidUserIdException.class, () ->
                iSyncTokenService.requestToken(user));
    }

    @Test
    void givenValidCredentialsAndUser_whenIssueToken_thenReturnUserToken() {
        String username = credentials.getUsername();
        when(userRepository.findByCredentialsUsername(username))
                .thenReturn(user);

        UserToken result = iSyncTokenService.issueToken(credentials);
        assertThat(result).isInstanceOf(UserToken.class);
        assertTrue(result.getToken().startsWith(user.getUserId()));
        assertThat(result.getToken().substring(user.getUserId().length() + 1))
                .matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"); // "yyyy-MM-dd'T'HH:mm:ssX"

        verify(userRepository, times(1)).findByCredentialsUsername(username);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenValidCredentialsAndNoUser_whenIssueToken_thenCreateUserAndReturnUserToken() {
        String username = credentials.getUsername();
        when(userRepository.findByCredentialsUsername(username))
                .thenReturn(null);

        UserToken result = iSyncTokenService.issueToken(credentials);
        assertThat(result).isInstanceOf(UserToken.class);
        assertTrue(result.getToken().startsWith(username));
        assertThat(result.getToken().substring(username.length() + 1))
                .matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"); // "yyyy-MM-dd'T'HH:mm:ssX"

        verify(userRepository, times(1)).findByCredentialsUsername(username);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void givenInvalidCredentials_whenIssueToken_thenThrowInvalidCredentialsException() {
        credentials.setPassword("random");

        assertThrows(InvalidCredentialsException.class, () ->
                iSyncTokenService.issueToken(credentials));

        verify(userRepository, never()).findByCredentialsUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenInvalidUserId_whenIssueToken_thenThrowInvalidCredentialsException() {
        String username = credentials.getUsername();
        user.setUserId("A");
        when(userRepository.findByCredentialsUsername(username))
                .thenReturn(user);

        assertThrows(InvalidUserIdException.class, () ->
                iSyncTokenService.issueToken(credentials));
    }

}