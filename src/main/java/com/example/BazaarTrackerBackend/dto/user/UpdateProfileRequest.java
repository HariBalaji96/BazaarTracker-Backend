package com.example.BazaarTrackerBackend.dto.user;

public class UpdateProfileRequest {

    private String name;

    public UpdateProfileRequest() {}

    // 🔹 Constructor (optional)
    public UpdateProfileRequest(String name) {
        this.name = name;
    }

    // ✅ Getter & Setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}