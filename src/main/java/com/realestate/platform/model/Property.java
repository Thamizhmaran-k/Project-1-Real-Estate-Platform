package com.realestate.platform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String location;

    private String imageUrl;

    // --- FIX: Enum Definition ---
    public enum PropertyType {
        SALE,
        RENT
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType type;
    // ----------------------------

    private boolean approved = false;

    private LocalDateTime dateListed;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public PropertyType getType() { return type; }
    public void setType(PropertyType type) { this.type = type; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    public LocalDateTime getDateListed() { return dateListed; }
    public void setDateListed(LocalDateTime dateListed) { this.dateListed = dateListed; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}