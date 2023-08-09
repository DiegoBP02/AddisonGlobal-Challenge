package com.example.demo.controllers;

import com.example.demo.ApplicationConfigTest;
import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.entities.UserToken;
import com.example.demo.exceptions.DelayInterruptedException;
import com.example.demo.exceptions.InvalidCredentialsException;
import com.example.demo.exceptions.InvalidUserIdException;
import com.example.demo.services.impl.ISyncTokenServiceImpl;
import com.example.demo.utils.TestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ApplicationConfigTest {
    private static final String PATH = "/auth";

    @MockBean
    private ISyncTokenServiceImpl iSyncTokenService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    User user = TestDataBuilder.buildUserWithId();
    Credentials credentials = TestDataBuilder.buildCredentials();
    UserToken userToken = TestDataBuilder.buildUserToken();

    private MockHttpServletRequestBuilder buildMockRequestPost
            (String endpoint, Object requestObject) throws Exception {
        return MockMvcRequestBuilders
                .post(PATH + endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestObject));
    }

    @Test
    void givenValidCredentials_whenIssueToken_thenReturnUserToken() throws Exception {
        when(iSyncTokenService.issueToken(credentials)).thenReturn(userToken);

        MockHttpServletRequestBuilder mockRequest = buildMockRequestPost
                ("/issueToken", credentials);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userToken)));

        verify(iSyncTokenService, times(1)).issueToken(credentials);
    }

    @Test
    void givenInvalidCredentials_whenIssueToken_thenHandleInvalidCredentialsException() throws Exception {
        when(iSyncTokenService.issueToken(credentials)).thenThrow(InvalidCredentialsException.class);

        MockHttpServletRequestBuilder mockRequest = buildMockRequestPost
                ("/issueToken", credentials);

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof InvalidCredentialsException));

        verify(iSyncTokenService, times(1)).issueToken(credentials);
    }

    @Test
    void givenInvalidUserId_whenIssueToken_thenHandleInvalidUserIdException() throws Exception {
        when(iSyncTokenService.issueToken(credentials)).thenThrow(InvalidUserIdException.class);

        MockHttpServletRequestBuilder mockRequest = buildMockRequestPost
                ("/issueToken", credentials);

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof InvalidUserIdException));

        verify(iSyncTokenService, times(1)).issueToken(credentials);
    }

    @Test
    void givenInvalidBody_whenIssueToken_thenHandleMethodArgumentNotValidException() throws Exception {
        Credentials invalidCredentials = new Credentials();

        MockHttpServletRequestBuilder mockRequest = buildMockRequestPost
                ("/issueToken", invalidCredentials);

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException));

        verify(iSyncTokenService, never()).issueToken(credentials);
    } @Test
    void givenDelayError_whenIssueToken_thenHandleDelayInterruptedException() throws Exception {
        when(iSyncTokenService.issueToken(credentials)).thenThrow(DelayInterruptedException.class);

        MockHttpServletRequestBuilder mockRequest = buildMockRequestPost
                ("/issueToken", credentials);

        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(result ->
                        assertTrue(result.getResolvedException()
                                instanceof DelayInterruptedException));

        verify(iSyncTokenService, times(1)).issueToken(credentials);
    }

}