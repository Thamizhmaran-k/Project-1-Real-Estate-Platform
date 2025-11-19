package com.realestate.platform.service;

import com.realestate.platform.model.Property;
import com.realestate.platform.model.User;
import com.realestate.platform.repository.PropertyRepository;
import com.realestate.platform.service.impl.PropertyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceImplTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    @Test
    public void whenSaveProperty_thenSetsDateAndSaves() {
        Property property = new Property();
        property.setTitle("New Prop");

        propertyService.saveProperty(property);

        verify(propertyRepository, times(1)).save(property);
        assertNotNull(property.getDateListed()); // Ensures date logic works
    }

    @Test
    public void whenApproveProperty_thenSetsApprovedToTrueAndSaves() {
        Property property = new Property();
        property.setId(1L);
        property.setApproved(false);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        propertyService.approveProperty(1L);

        assertTrue(property.isApproved());
        verify(propertyRepository).save(property);
    }

    @Test
    public void whenDeleteProperty_thenDeletesFromRepo() {
        Long propId = 1L;
        propertyService.deleteProperty(propId);
        verify(propertyRepository, times(1)).deleteById(propId);
    }
}