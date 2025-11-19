package com.realestate.platform.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // We use @Value to inject the properties from application.properties
    public CloudinaryService(
            @org.springframework.beans.factory.annotation.Value("${cloudinary.cloud_name}") String cloudName,
            @org.springframework.beans.factory.annotation.Value("${cloudinary.api_key}") String apiKey,
            @org.springframework.beans.factory.annotation.Value("${cloudinary.api_secret}") String apiSecret) {
        
        Map<String, String> config = Map.of(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        );
        this.cloudinary = new Cloudinary(config);
    }

    /**
     * Uploads a file to Cloudinary.
     * @param file The image file to upload.
     * @return The secure URL of the uploaded image.
     * @throws IOException if the upload fails.
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // We upload the file's bytes and get the response
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        
        // The response map contains the 'secure_url' of the uploaded image
        return uploadResult.get("secure_url").toString();
    }
}