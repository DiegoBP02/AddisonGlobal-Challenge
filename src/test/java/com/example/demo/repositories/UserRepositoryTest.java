package com.example.demo.repositories;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.utils.TestDataBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    void whenFindByCredentialsUsername_givenUser_shouldReturnUser() {
        Credentials credentialsBuilder = TestDataBuilder.buildCredentials();
        User user = TestDataBuilder.buildUserNoId();
        user.setCredentials(credentialsBuilder);
        credentialsBuilder.setUser(user);

        userRepository.save(user);
        User result = userRepository.findByCredentialsUsername(credentialsBuilder.getUsername());
        assertEquals(user, result);
    }
}