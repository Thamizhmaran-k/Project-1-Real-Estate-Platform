package com.realestate.platform.controller;

import com.realestate.platform.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/")
    public String index(Model model) {
        // Fetch featured properties (e.g., top 3 or random)
        // For now, we just show all approved properties
        model.addAttribute("properties", propertyService.findAllApproved());
        return "index"; // This looks for templates/index.html
    }
}