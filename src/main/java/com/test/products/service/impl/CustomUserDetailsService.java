package com.test.products.service.impl;

import com.test.products.model.entity.User;
import com.test.products.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final String STUDENT_NOT_FOUND_BY_USERNAME = "Cannot find user with username = ";

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND_BY_USERNAME + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(student.getUsername())
                .password(student.getPassword())
                .roles()
                .build();
    }

}