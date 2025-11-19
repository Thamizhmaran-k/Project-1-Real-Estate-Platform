package com.realestate.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier; // <--- ADD THIS IMPORT
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    // FIX: Use @Qualifier to specify the actual service implementation bean name
    // The bean name for a class named UserServiceImpl is automatically 'userServiceImpl'
    @Autowired
    @Qualifier("userServiceImpl") // <--- THIS TELLS SPRING EXACTLY WHICH BEAN TO USE
    private com.realestate.platform.service.UserService userService;

    @Autowired
    private com.realestate.platform.service.PropertyService propertyService;

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        // We try-catch here so if fetching fails, it doesn't crash the whole page
        try {
            model.addAttribute("users", userService.findAll());
            model.addAttribute("properties", propertyService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading admin data: " + e.getMessage());
        }
        return "admin";
    }

    // Simple method to delete property
    @PostMapping("/admin/delete-property/{id}")
    public String deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return "redirect:/admin";
    }
}