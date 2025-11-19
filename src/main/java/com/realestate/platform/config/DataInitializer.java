package com.realestate.platform.config;

import com.realestate.platform.model.Role;
import com.realestate.platform.model.User;
import com.realestate.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile; // Keep this if you want to skip tests
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
// Remove @Profile("!test") temporarily if you want to force it to run even during tests to debug,
// but for running the main app, it should work fine.
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String email = "admin@example.com";
        String rawPassword = "admin123";

        System.out.println("----------------------------------------------------------");
        System.out.println("   STARTING DATA INITIALIZER Check");
        System.out.println("----------------------------------------------------------");

        Optional<User> existing = userRepository.findByEmail(email);
        
        if (existing.isEmpty()) {
            User admin = new User();
            admin.setName("Super Admin");
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(rawPassword));
            admin.setRole(Role.ROLE_ADMIN);
            userRepository.save(admin);
            System.out.println("   >>> SUCCESS: Admin User CREATED.");
        } else {
            User admin = existing.get();
            admin.setPassword(passwordEncoder.encode(rawPassword));
            admin.setRole(Role.ROLE_ADMIN);
            userRepository.save(admin);
            System.out.println("   >>> SUCCESS: Admin User UPDATED (Password Reset).");
        }
        
        System.out.println("   Email: " + email);
        System.out.println("   Pass : " + rawPassword);
        System.out.println("----------------------------------------------------------");
    }
}