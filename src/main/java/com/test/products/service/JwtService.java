package com.test.products.service;

import com.test.products.model.payload.AuthRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateToken(AuthRequest authRequest);

    String generateRefreshToken(AuthRequest authRequest);

}
