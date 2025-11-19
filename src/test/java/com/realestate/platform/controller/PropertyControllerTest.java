package com.realestate.platform.controller;

import com.realestate.platform.config.SecurityConfig;
import com.realestate.platform.config.TestConfig;
import com.realestate.platform.model.Property;
import com.realestate.platform.service.PropertyService;
import com.realestate.platform.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropertyController.class)
@Import({TestConfig.class, SecurityConfig.class})
public class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    public void testListProperties() throws Exception {
        Property p1 = new Property();
        p1.setTitle("Test Property");
        List<Property> properties = Arrays.asList(p1);

        when(propertyService.findAllApproved()).thenReturn(properties);

        mockMvc.perform(get("/properties"))
                .andExpect(status().isOk())
                .andExpect(view().name("properties"))
                .andExpect(content().string(containsString("Test Property")));
    }

    @Test
    @WithMockUser
    public void testSearchProperties() throws Exception {
        Property p1 = new Property();
        p1.setTitle("Searched Villa");
        List<Property> properties = Arrays.asList(p1);

        when(propertyService.searchProperties(anyString())).thenReturn(properties);

        mockMvc.perform(get("/properties")
                .param("search", "Villa"))
                .andExpect(status().isOk())
                .andExpect(view().name("properties"))
                .andExpect(content().string(containsString("Searched Villa")));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testShowAddPropertyForm() throws Exception {
        mockMvc.perform(get("/dashboard/add-property"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-property"));
    }
}