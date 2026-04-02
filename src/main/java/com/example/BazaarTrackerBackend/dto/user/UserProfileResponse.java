package com.example.BazaarTrackerBackend.dto.user;

import com.example.BazaarTrackerBackend.model.enums.UserRole;

public class UserProfileResponse {

    private String id;
    private String name;
    private String email;
    private UserRole role;
    private boolean isActive;

    public UserProfileResponse() {}

    // 🔹 All-args constructor (optional but useful)
    public UserProfileResponse(String id, String name, String email, UserRole role, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
    }

    // ✅ Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}