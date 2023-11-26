package com.test.products.service.impl;

import com.test.products.exception.UserProcessingException;
import com.test.products.model.entity.Token;
import com.test.products.model.entity.User;
import com.test.products.model.enumeration.TokenType;
import com.test.products.model.payload.AuthRequest;
import com.test.products.model.payload.AuthResponse;
import com.test.products.repository.TokenRepository;
import com.test.products.repository.UserRepository;
import com.test.products.service.JwtService;
import com.test.products.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private static final String USERNAME_ALREADY_IN_USE = "Error: Email is already in use!";

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse addUser(AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            log.error(USERNAME_ALREADY_IN_USE);
            throw new UserProcessingException(USERNAME_ALREADY_IN_USE);
        }
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(request);
        var refreshToken = jwtService.generateRefreshToken(request);
        saveUserToken(savedUser, jwtToken);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
