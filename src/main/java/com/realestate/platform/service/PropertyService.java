package com.realestate.platform.service;

import com.realestate.platform.model.Property;
import java.util.List;

public interface PropertyService {
    List<Property> findAll();
    List<Property> findAllApproved();
    void saveProperty(Property property);
    Property findById(Long id);
    void deleteProperty(Long id);
    
    // This simple signature matches what your Controller is calling
    List<Property> searchProperties(String keyword);
    
    List<Property> findMyProperties();
    void approveProperty(Long id);
}