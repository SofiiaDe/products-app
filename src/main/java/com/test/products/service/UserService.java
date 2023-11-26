package com.test.products.service;

import com.test.products.model.payload.AuthRequest;
import com.test.products.model.payload.AuthResponse;

public interface UserService {

    AuthResponse addUser(AuthRequest request);

}
