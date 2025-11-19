package com.realestate.platform.controller.api;

import com.realestate.platform.model.Property;
import com.realestate.platform.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropertyApiController.class)
public class PropertyApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;


    @Test
    @WithMockUser
    public void testSearchProperties() throws Exception {
        Property p1 = new Property();
        p1.setTitle("Test House");
        List<Property> properties = Arrays.asList(p1);

        when(propertyService.searchProperties(anyString())).thenReturn(properties);

        mockMvc.perform(get("/api/properties/search")
                .param("keyword", "Test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test House"));
    }

    @Test
    @WithMockUser
    public void testGetPropertyById() throws Exception {
        Property p1 = new Property();
        p1.setId(1L);
        p1.setTitle("Unique House");

        when(propertyService.findById(1L)).thenReturn(p1);

        mockMvc.perform(get("/api/properties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Unique House"));
    }
}