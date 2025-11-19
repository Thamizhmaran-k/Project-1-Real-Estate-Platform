package com.realestate.platform.repository;

import com.realestate.platform.config.TestConfig;
import com.realestate.platform.model.Role;
import com.realestate.platform.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@Import(TestConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@example.com");
        user.setPassword("securePassword123"); 
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);

        User found = userRepository.findByEmail("test@example.com").orElse(null);
        assertNotNull(found);
        assertThat(found.getEmail()).isEqualTo("test@example.com");
    }
}