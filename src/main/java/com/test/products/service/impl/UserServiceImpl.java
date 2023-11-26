package com.test.products.service.impl;

import com.test.products.exception.EntityNotFoundException;
import com.test.products.exception.UserProcessingException;
import com.test.products.model.entity.User;
import com.test.products.model.payload.AuthRequest;
import com.test.products.model.payload.AuthResponse;
import com.test.products.repository.UserRepository;
import com.test.products.service.JwtService;
import com.test.products.service.TokenService;
import com.test.products.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private static final String USERNAME_ALREADY_IN_USE = "Error: Username is already in use!";
    private static final String USER_NOT_FOUND_BY_USERNAME = "Cannot find user with username = ";

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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
        tokenService.saveUserToken(savedUser, jwtToken);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(), authRequest.getPassword()
                )
        );

        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_USERNAME + authRequest.getUsername()));

        String jwtToken = jwtService.generateToken(authRequest);
        String refreshToken = jwtService.generateRefreshToken(authRequest);
        tokenService.revokeAllUserTokens(user.getId());
        tokenService.saveUserToken(user, jwtToken);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

}
