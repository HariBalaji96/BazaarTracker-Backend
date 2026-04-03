package com.example.bazaarTrackerBackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials.path:}")
    private String firebaseCredentialsPath;

    @Bean
    public Firestore firestore() throws IOException {
        GoogleCredentials credentials = null;

        // 1. Try reading from a raw JSON environment variable (Easiest for Render Dashboard)
        String envJson = System.getenv("FIREBASE_CREDENTIALS_JSON");
        if (envJson != null && !envJson.isBlank()) {
            try {
                credentials = GoogleCredentials.fromStream(new java.io.ByteArrayInputStream(envJson.getBytes()));
                System.out.println("Loaded Firebase credentials from FIREBASE_CREDENTIALS_JSON environment variable.");
            } catch (Exception e) {
                System.out.println("Failed to parse FIREBASE_CREDENTIALS_JSON: " + e.getMessage());
            }
        }

        // 2. Try reading from the local application.properties file path
        if (credentials == null && firebaseCredentialsPath != null && !firebaseCredentialsPath.isEmpty()) {
            try {
                FileInputStream serviceAccount = new FileInputStream(firebaseCredentialsPath);
                credentials = GoogleCredentials.fromStream(serviceAccount);
            } catch (Exception e) {
                System.out.println("Could not load Firebase file from path. Falling back to Default Credentials...");
            }
        }

        // If file didn't exist (e.g. running in Docker/Render), use Application Default Credentials
        if (credentials == null) {
            credentials = GoogleCredentials.getApplicationDefault();
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirestoreClient.getFirestore();
    }
}