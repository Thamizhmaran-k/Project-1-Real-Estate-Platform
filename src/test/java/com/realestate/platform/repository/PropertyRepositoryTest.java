package com.realestate.platform.repository;

import com.realestate.platform.config.TestConfig;
import com.realestate.platform.model.Property;
import com.realestate.platform.model.Role;
import com.realestate.platform.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
public class PropertyRepositoryTest {

    @Autowired
    private PropertyRepository propertyRepository;
    
    @Autowired // <-- UserRepository was missing autowiring in the log error
    private UserRepository userRepository;

    @Test
    public void testFindApproved() {
        // 1. Create Owner
        User owner = new User();
        owner.setName("Landlord");
        owner.setEmail("landlord@test.com");
        owner.setPassword("securePassword123"); 
        owner.setRole(Role.ROLE_USER);
        userRepository.save(owner);

        // 2. Create Property
        Property p = new Property();
        p.setTitle("Approved House");
        p.setApproved(true);
        p.setPrice(100000.0);
        p.setDescription("Description");
        p.setLocation("New York");
        p.setImageUrl("img.jpg");
        // Assuming PropertyType is defined inside the Property class or imported correctly
        p.setType(Property.PropertyType.SALE); 
        
        p.setOwner(owner);

        propertyRepository.save(p);

        // 3. Assert
        List<Property> found = propertyRepository.findByApprovedTrue();
        assertThat(found).isNotEmpty();
    }

    @Test
    public void testSearch() {
        // 1. Create Owner
        User owner = new User();
        owner.setName("Seller");
        owner.setEmail("seller@test.com");
        owner.setPassword("securePassword123");
        owner.setRole(Role.ROLE_USER);
        userRepository.save(owner);

        // 2. Create Property
        Property p = new Property();
        p.setTitle("Luxury Villa");
        p.setLocation("Miami");
        p.setDescription("Beautiful place");
        p.setApproved(true);
        p.setPrice(500000.0);
        p.setImageUrl("img.jpg");
        p.setType(Property.PropertyType.SALE);
        
        p.setOwner(owner);

        propertyRepository.save(p);

        // 3. Assert
        List<Property> result = propertyRepository.search("Luxury");
        assertThat(result).isNotEmpty();
    }
}