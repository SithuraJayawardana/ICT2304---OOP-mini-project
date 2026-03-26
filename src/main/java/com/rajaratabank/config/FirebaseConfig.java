package com.rajaratabank.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Encapsulates the configuration and connection logic for Firebase.
 * Using Encapsulation to hide specific implementation and connection details,
 * exposing only the necessary initialization method to the rest of the
 * application.
 */
public class FirebaseConfig {

    private static final String SERVICE_ACCOUNT_KEY_PATH = "firebase-service-account.json";
    private static boolean isInitialized = false;

    /**
     * Initializes the Firebase Admin SDK connection.
     */
    public static void initialize() {
        if (!isInitialized) {
            try {
                FileInputStream serviceAccount = new FileInputStream(SERVICE_ACCOUNT_KEY_PATH);

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://rajarata-digital-bank-default-rtdb.firebaseio.com")
                        .build();

                FirebaseApp.initializeApp(options);
                isInitialized = true;
                System.out.println("Firebase initialized successfully.");
            } catch (IOException e) {
                System.err.println("Firebase initialization failed: " + e.getMessage());
                // In a real application, we would throw a custom initialization exception here
            }
        }
    }
}
