package com.realestate.platform.controller;

import com.realestate.platform.config.SecurityConfig;
import com.realestate.platform.config.TestConfig;
import com.realestate.platform.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({TestConfig.class, SecurityConfig.class}) 
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void whenGetLoginPage_asAnonymous_thenReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser
    public void whenGetLoginPage_asLoggedInUser_thenRedirectHome() throws Exception {
        // FIX 1: The test was failing because the actual code redirects (302) to home ("/").
        mockMvc.perform(get("/login"))
               .andExpect(status().is3xxRedirection()) // Expect 302
               .andExpect(redirectedUrl("/"));        // Expect redirect to home
    }

    @Test
    public void whenGetRegisterPage_asAnonymous_thenReturnRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void whenPostRegister_withValidData_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("name", "John")
                .param("email", "john@test.com")
                .param("password", "pass123")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                // FIX 2: The controller redirects to /login. We match that exact redirect path.
                .andExpect(redirectedUrl("/login")); 
    }
}