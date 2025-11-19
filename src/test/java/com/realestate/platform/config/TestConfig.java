package com.realestate.platform.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public PasswordEncoder mockPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public UserDetailsService mockUserDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("testuser")
                .password(mockPasswordEncoder().encode("password"))
                .roles("USER")
                .build()
        );
    }
}