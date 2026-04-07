package com.example.BazaarTrackerBackend.dto.user;

public class UpdateProfileRequest {

    private String name;
    private String companyName;

    public UpdateProfileRequest() {}

    // 🔹 Constructor (optional)
    public UpdateProfileRequest(String name, String companyName) {
        this.name = name;
        this.companyName = companyName;
    }

    // ✅ Getter & Setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}