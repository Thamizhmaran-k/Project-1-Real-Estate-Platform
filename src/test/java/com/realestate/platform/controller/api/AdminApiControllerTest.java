package com.realestate.platform.controller.api;

import com.realestate.platform.config.SecurityConfig;
import com.realestate.platform.config.TestConfig;
import com.realestate.platform.model.Role;
import com.realestate.platform.model.User;
import com.realestate.platform.service.PropertyService;
import com.realestate.platform.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminApiController.class)
@Import({TestConfig.class, SecurityConfig.class})
public class AdminApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PropertyService propertyService;

    @Test
    @WithMockUser(roles = "ADMIN") 
    public void whenGetApiAdminUsers_asAdmin_thenReturnUserList() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setRole(Role.ROLE_USER);
        List<User> users = Arrays.asList(user);

        when(userService.findAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    @WithMockUser(roles = "USER") 
    public void whenGetApiAdminUsers_asUser_thenIsForbidden() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isForbidden());
    }
}