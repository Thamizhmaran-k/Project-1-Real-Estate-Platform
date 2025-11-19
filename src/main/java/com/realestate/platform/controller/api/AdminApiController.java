package com.realestate.platform.controller.api;

import com.realestate.platform.model.User;
import com.realestate.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    // FIX: Use @Qualifier to specify the actual service implementation bean name
    @Autowired
    @Qualifier("userServiceImpl") // <--- THIS RESOLVES THE AMBIGUITY
    private UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }
}