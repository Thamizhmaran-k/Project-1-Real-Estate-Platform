package com.realestate.platform.controller;

import com.realestate.platform.service.PropertyService;
import com.realestate.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    @Autowired
    @Qualifier("userServiceImpl") // Keeps the Bean Ambiguity fix
    private UserService userService;

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')") // Extra security check
    public String adminPanel(Model model) {
        try {
            model.addAttribute("users", userService.findAllUsers());
            model.addAttribute("properties", propertyService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading admin data: " + e.getMessage());
        }
        return "admin";
    }

    // --- DELETE PROPERTY ---
    @PostMapping("/admin/delete-property/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return "redirect:/admin";
    }

    // --- FIX: THIS WAS MISSING ---
    @PostMapping("/admin/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveProperty(@PathVariable Long id) {
        propertyService.approveProperty(id);
        return "redirect:/admin";
    }
}