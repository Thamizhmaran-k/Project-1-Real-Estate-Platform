package com.realestate.platform.controller;

import com.realestate.platform.dto.RegisterDto;
import com.realestate.platform.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier; // <--- ADD THIS IMPORT
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;

    // FIX: Apply @Qualifier to the constructor parameter to resolve ambiguity
    public AuthController(@Qualifier("userServiceImpl") UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerDto") RegisterDto registerDto,
                               BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "register";
        }

        // Basic check to see if user already exists
        if (userService.findByEmail(registerDto.getEmail()) != null) {
            model.addAttribute("emailError", "Email address is already registered.");
            return "register";
        }

        userService.registerUser(registerDto);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
        return "redirect:/login";
    }
}