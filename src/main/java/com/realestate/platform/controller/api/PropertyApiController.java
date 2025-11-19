package com.realestate.platform.controller.api;

import com.realestate.platform.model.Property;
import com.realestate.platform.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyApiController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public List<Property> getAllProperties() {
        return propertyService.findAllApproved();
    }

    // FIX: Simplified search to match Service
    @GetMapping("/search")
    public List<Property> searchProperties(@RequestParam String keyword) {
        return propertyService.searchProperties(keyword);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        // FIX: Handle the 'Property' return type safely
        Property property = propertyService.findById(id);
        if (property != null) {
            return ResponseEntity.ok(property);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}