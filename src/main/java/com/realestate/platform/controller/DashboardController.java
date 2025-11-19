package com.realestate.platform.controller;

import com.realestate.platform.model.Property;
import com.realestate.platform.model.User;
import com.realestate.platform.service.PropertyService;
import com.realestate.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    // FIX: Apply @Qualifier to resolve ambiguity with 'userDetailsService'
    @Autowired
    @Qualifier("userServiceImpl") // <--- THIS TELLS SPRING EXACTLY WHICH BEAN TO USE
    private UserService userService;

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public String dashboard(Model model) {
        // Need to handle null user just in case, though security should prevent it
        User user = userService.getCurrentUser(); 
        
        model.addAttribute("user", user);

        List<Property> myProperties = propertyService.findMyProperties();
        model.addAttribute("myProperties", myProperties);

        return "dashboard";
    }
}