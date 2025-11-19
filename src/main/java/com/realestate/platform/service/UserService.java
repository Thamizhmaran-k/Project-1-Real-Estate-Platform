package com.realestate.platform.service;

import com.realestate.platform.dto.RegisterDto;
import com.realestate.platform.model.User;
import java.util.List;

public interface UserService {
    
    // Standard Database operations
    void save(User user);
    User findByEmail(String email);
    void deleteUser(Long id);

    // Registration logic
    void registerUser(RegisterDto registerDto);

    // Helper method to get the user who is currently logged in
    User getCurrentUser();

    // For Admin Panel (The controllers were asking for both names, so I included both)
    List<User> findAll();
    List<User> findAllUsers();
}