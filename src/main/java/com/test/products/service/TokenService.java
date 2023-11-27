package com.test.products.service;

import com.test.products.model.entity.User;

public interface TokenService {

    void saveUserToken(User user, String jwtToken);

    void revokeAllUserTokens(Long userId);

}
