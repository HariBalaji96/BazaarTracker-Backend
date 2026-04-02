package com.example.BazaarTrackerBackend.constants;

public class AppConstants {

    private AppConstants() {
        // prevent instantiation
    }

    // 🔐 SECURITY
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    // 📊 DEFAULT VALUES
    public static final double ZERO_AMOUNT = 0.0;

    // 📌 ERROR MESSAGES
    public static final String USER_NOT_FOUND = "User not found";
    public static final String VENDOR_NOT_FOUND = "Vendor not found";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String INVALID_CREDENTIALS = "Invalid email or password";

    // 💰 BUSINESS VALIDATION
    public static final String INSUFFICIENT_STOCK = "Insufficient stock";
    public static final String INVALID_PAYMENT = "Payment amount must be greater than 0";
    public static final String OVER_PAYMENT = "Payment exceeds pending amount";

}