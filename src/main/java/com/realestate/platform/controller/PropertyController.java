package com.realestate.platform.controller;

import com.realestate.platform.model.Property;
import com.realestate.platform.model.Role;
import com.realestate.platform.model.User;
import com.realestate.platform.service.PropertyService;
import com.realestate.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    public static String UPLOAD_DIRECTORY = "uploads";

    // --- PUBLIC LISTING PAGE ---
    @GetMapping("/properties")
    public String listProperties(Model model, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("properties", propertyService.searchProperties(search));
        } else {
            model.addAttribute("properties", propertyService.findAllApproved());
        }
        return "properties";
    }

    // --- SINGLE PROPERTY DETAILS PAGE ---
    @GetMapping("/properties/{id}")
    public String propertyDetails(@PathVariable Long id, Model model) {
        Property property = propertyService.findById(id);
        if (property != null) {
            model.addAttribute("property", property);
            return "property-detail"; // You will need to create this file next
        }
        return "redirect:/properties";
    }

    // --- ADD PROPERTY FORM ---
    @GetMapping("/dashboard/add-property")
    public String showAddPropertyForm(Model model) {
        model.addAttribute("property", new Property());
        return "add-property";
    }

    // --- EDIT PROPERTY FORM (New) ---
    @GetMapping("/dashboard/edit/{id}")
    public String showEditPropertyForm(@PathVariable Long id, Model model) {
        Property property = propertyService.findById(id);
        User currentUser = userService.getCurrentUser();

        // Security Check: Only owner or admin can edit
        if (property != null && (property.getOwner().getId().equals(currentUser.getId()) || currentUser.getRole() == Role.ROLE_ADMIN)) {
            model.addAttribute("property", property);
            return "add-property"; // Reuse the add form for editing
        }
        return "redirect:/dashboard?error=unauthorized";
    }

    // --- DELETE PROPERTY (New) ---
    @PostMapping("/dashboard/delete/{id}")
    public String deleteProperty(@PathVariable Long id) {
        Property property = propertyService.findById(id);
        User currentUser = userService.getCurrentUser();

        if (property != null && (property.getOwner().getId().equals(currentUser.getId()) || currentUser.getRole() == Role.ROLE_ADMIN)) {
            propertyService.deleteProperty(id);
            return "redirect:/dashboard?deleted";
        }
        return "redirect:/dashboard?error=unauthorized";
    }

    // --- SAVE/UPDATE LOGIC ---
    @PostMapping("/dashboard/add-property")
    public String addProperty(@ModelAttribute Property property, 
                              @RequestParam("image") MultipartFile file) {
        try {
            User user = userService.getCurrentUser();
            
            // If editing, preserve existing owner and details if not overwritten
            if (property.getId() != null) {
                Property existing = propertyService.findById(property.getId());
                if (existing != null) {
                    property.setOwner(existing.getOwner()); // Keep original owner
                    property.setDateListed(existing.getDateListed()); // Keep original date
                    if (file.isEmpty()) {
                        property.setImageUrl(existing.getImageUrl()); // Keep old image if no new one uploaded
                    }
                }
            } else {
                property.setOwner(user); // New property gets current user
            }

            // Handle Image Upload
            if (!file.isEmpty()) {
                String projectPath = System.getProperty("user.dir");
                Path uploadPath = Paths.get(projectPath, UPLOAD_DIRECTORY);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());
                property.setImageUrl("/uploads/" + fileName);
            }

            // Reset approval on edit (optional business logic)
            if (user.getRole() == Role.ROLE_ADMIN) {
                property.setApproved(true);
            } else {
                property.setApproved(false); // Require re-approval if edited
            }

            propertyService.saveProperty(property);
            
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/dashboard?error=upload_failed";
        }

        return "redirect:/dashboard?success";
    }
}