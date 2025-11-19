package com.realestate.platform.service.impl;

import com.realestate.platform.model.Property; 
import com.realestate.platform.model.User;   
import com.realestate.platform.repository.PropertyRepository;
import com.realestate.platform.service.PropertyService;
import com.realestate.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier; // <--- NEW IMPORT
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    // FIX: Use @Qualifier to specify the correct bean name 'userServiceImpl' 
    // to resolve the ambiguity with the 'userDetailsService' bean.
    @Autowired
    @Qualifier("userServiceImpl") 
    private UserService userService;

    @Override
    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    @Override
    public List<Property> findAllApproved() {
        return propertyRepository.findByApprovedTrue();
    }
    
    @Override
    public void saveProperty(Property property) {
        if (property.getDateListed() == null) {
            property.setDateListed(LocalDateTime.now());
        }
        propertyRepository.save(property);
    }
    
    @Override
    public Property findById(Long id) {
        return propertyRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteProperty(Long id) {
        if (id != null) {
            propertyRepository.deleteById(id);
        }
    }

    @Override
    public List<Property> findMyProperties() {
        User currentUser = userService.getCurrentUser();
        // Assuming your repository has a method like findByOwnerId(Long) 
        // to retrieve properties by the owner's ID.
        return propertyRepository.findByOwnerId(currentUser.getId()); 
    }

    @Override
    public List<Property> searchProperties(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return propertyRepository.findByApprovedTrue();
        }
        return propertyRepository.search(keyword);
    }

    @Override
    public void approveProperty(Long id) {
        Property prop = propertyRepository.findById(id).orElse(null);
        if (prop != null) {
            prop.setApproved(true);
            propertyRepository.save(prop);
        }
    }
}