package com.realestate.platform.controller;

import com.realestate.platform.model.Role;
import com.realestate.platform.model.User;
import com.realestate.platform.service.PropertyService;
import com.realestate.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private PropertyService propertyService;

    // --- ADMIN DASHBOARD ---
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
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

    // --- PROPERTY ACTIONS ---
    @PostMapping("/admin/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveProperty(@PathVariable Long id) {
        propertyService.approveProperty(id);
        return "redirect:/admin?approved";
    }

    @PostMapping("/admin/delete-property/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return "redirect:/admin?deleted";
    }

    // --- USER ACTIONS (NEW REQUIREMENT) ---

    // 1. Delete User
    @PostMapping("/admin/delete-user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin?userDeleted";
    }

    // 2. Show Edit Role Form
    @GetMapping("/admin/edit-user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        // We need to find the user first. We might need to add findById to UserService if missing.
        // For now, we filter from the list or assume findById exists.
        // Let's implement a quick find logic using the existing service or repository directly if needed.
        // Better practice: Add findById to UserService.
        
        // Assuming findById isn't in your service interface yet, let's find it safely:
        Optional<User> userOpt = userService.findAllUsers().stream()
                .filter(u -> u.getId().equals(id)).findFirst();
                
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            model.addAttribute("roles", Role.values()); // Pass all available roles
            return "user-edit-form"; // We need to create this template
        }
        
        return "redirect:/admin?error=UserNotFound";
    }

    // 3. Save User Role Change
    @PostMapping("/admin/edit-user")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserRole(@ModelAttribute User formUser) {
        // We need to fetch the actual user and just update the role
        Optional<User> originalUserOpt = userService.findAllUsers().stream()
                .filter(u -> u.getId().equals(formUser.getId())).findFirst();

        if (originalUserOpt.isPresent()) {
            User originalUser = originalUserOpt.get();
            originalUser.setRole(formUser.getRole());
            userService.save(originalUser); // Save the updated role
        }
        return "redirect:/admin?roleUpdated";
    }
}
