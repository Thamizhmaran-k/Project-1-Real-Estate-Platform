package com.realestate.platform.controller;

import com.realestate.platform.model.Property;
import com.realestate.platform.model.Role;
import com.realestate.platform.model.User;
import com.realestate.platform.service.CloudinaryService; // <--- Must have this
import com.realestate.platform.service.PropertyService;
import com.realestate.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService; // <--- Inject Cloudinary

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
            return "property-detail";
        }
        return "redirect:/properties";
    }

    // --- ADD PROPERTY FORM ---
    @GetMapping("/dashboard/add-property")
    public String showAddPropertyForm(Model model) {
        model.addAttribute("property", new Property());
        return "add-property";
    }

    // --- EDIT PROPERTY FORM ---
    @GetMapping("/dashboard/edit/{id}")
    public String showEditPropertyForm(@PathVariable Long id, Model model) {
        Property property = propertyService.findById(id);
        User currentUser = userService.getCurrentUser();

        if (property != null && (property.getOwner().getId().equals(currentUser.getId()) || currentUser.getRole() == Role.ROLE_ADMIN)) {
            model.addAttribute("property", property);
            return "add-property";
        }
        return "redirect:/dashboard?error=unauthorized";
    }

    // --- DELETE PROPERTY ---
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

    // --- SAVE/UPDATE LOGIC (WITH CLOUDINARY) ---
    @PostMapping("/dashboard/add-property")
    public String addProperty(@ModelAttribute Property property, 
                              @RequestParam("image") MultipartFile file) {
        try {
            User user = userService.getCurrentUser();
            
            // If editing, preserve details
            if (property.getId() != null) {
                Property existing = propertyService.findById(property.getId());
                if (existing != null) {
                    property.setOwner(existing.getOwner());
                    property.setDateListed(existing.getDateListed());
                    // Keep old image if no new one uploaded
                    if (file.isEmpty()) {
                        property.setImageUrl(existing.getImageUrl()); 
                    }
                }
            } else {
                property.setOwner(user);
            }

            // --- CLOUDINARY UPLOAD ---
            if (!file.isEmpty()) {
                // This uploads to the internet and gives back a valid URL (starting with http)
                String imageUrl = cloudinaryService.uploadFile(file);
                property.setImageUrl(imageUrl);
            }
            // -------------------------

            // Reset approval logic
            if (user.getRole() == Role.ROLE_ADMIN) {
                property.setApproved(true);
            } else {
                property.setApproved(false);
            }

            propertyService.saveProperty(property);
            
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/dashboard?error=upload_failed";
        }

        return "redirect:/dashboard?success";
    }
}