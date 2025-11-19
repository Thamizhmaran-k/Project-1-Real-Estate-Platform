package com.realestate.platform.repository;

import com.realestate.platform.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    
    // Fixes "findByApprovedTrue is undefined"
    List<Property> findByApprovedTrue();
    
    List<Property> findByOwnerId(Long ownerId);

    // Fixes "search method undefined"
    @Query("SELECT p FROM Property p WHERE " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Property> search(String keyword);
}