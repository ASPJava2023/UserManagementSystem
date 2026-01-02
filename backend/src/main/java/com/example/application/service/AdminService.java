package com.example.application.service;

import com.example.application.dto.request.UserRequest;
import com.example.application.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Page<UserResponse> getAllUsers(String search, Pageable pageable);

    UserResponse getUserById(Long id);

    UserResponse createUser(UserRequest request);

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);
}
