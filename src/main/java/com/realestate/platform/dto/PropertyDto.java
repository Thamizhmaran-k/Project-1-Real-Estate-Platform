package com.realestate.platform.dto;

import com.realestate.platform.model.PropertyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PropertyDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Property type is required")
    private PropertyType type; // SALE or RENT

    @NotBlank(message = "Location is required")
    private String location;

    // --- CHANGE ---
    // This is no longer @NotNull. It's optional for editing.
    private MultipartFile image; 
}