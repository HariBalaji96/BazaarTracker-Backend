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

        // Try reading from the specified file path locally
        if (firebaseCredentialsPath != null && !firebaseCredentialsPath.isEmpty()) {
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