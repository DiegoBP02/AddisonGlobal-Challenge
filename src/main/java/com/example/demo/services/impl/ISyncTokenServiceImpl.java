package com.example.demo.services.impl;

import com.example.demo.entities.Credentials;
import com.example.demo.entities.User;
import com.example.demo.entities.UserToken;
import com.example.demo.exceptions.DelayInterruptedException;
import com.example.demo.exceptions.InvalidCredentialsException;
import com.example.demo.exceptions.InvalidUserIdException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ISyncTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class ISyncTokenServiceImpl implements ISyncTokenService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ISyncTokenServiceImpl.class);

    @Override
    public User authenticate(Credentials credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        if (!username.toUpperCase().equals(password)) {
            String errorMessage = "Invalid credentials";
            logger.error(errorMessage);
            throw new InvalidCredentialsException(errorMessage);
        }

        User user;

        user = userRepository.findByCredentialsUsername(username);

        if (user == null) {
            Credentials credentialsBuilder = Credentials.builder()
                    .username(username)
                    .password(password)
                    .build();

            user = User.builder()
                    .userId(username)
                    .credentials(credentialsBuilder)
                    .build();

            credentialsBuilder.setUser(user);

            userRepository.save(user);
        }

        randomDelay();

        return user;
    }

    @Override
    public UserToken requestToken(User user) {
        String userId = user.getUserId();
        if (userId.startsWith("A")) {
            String errorMessage = "The userId can't start with 'A'";
            logger.error(errorMessage);
            throw new InvalidUserIdException(errorMessage);
        }

        OffsetDateTime currentOffsetTime = OffsetDateTime.now(ZoneOffset.UTC);
        String pattern = "yyyy-MM-dd'T'HH:mm:ssX";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedTime = currentOffsetTime.format(formatter);

        String token = userId + "_" + formattedTime;

        UserToken userToken = new UserToken(token);

        randomDelay();

        return userToken;
    }

    private void randomDelay() {
        Random random = new Random();
        int delay = random.nextInt(5000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            String errorMessage = "InterruptedException occurred during delay";
            logger.error(errorMessage, e);
            Thread.currentThread().interrupt();
            throw new DelayInterruptedException(errorMessage, e);
        }

        logger.info("Time of random delay: {} ms", delay);
    }

}
