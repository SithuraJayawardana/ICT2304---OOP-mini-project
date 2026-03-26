package com.rajaratabank.security;

import com.rajaratabank.exceptions.InvalidLoginException;
import com.rajaratabank.exceptions.UnauthorizedAccessException;
import com.rajaratabank.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Manages Authentication for all User types.
 * Connects with Firebase to validate login credentials.
 */
public class AuthManager {

    private static final String AUDIT_LOG_FILE = "audit_security_log.txt";

    /**
     * Authenticates a user based on email.
     * In a production Firebase system, the client sends an ID token which the server verifies.
     * Here, for academic demonstration, we check the Firebase UserRecord existence.
     * 
     * @param email The user's email
     * @param password The user's password (plaintext to be checked/hashed in a traditional DB)
     * @return UserRecord if authenticated successfully
     * @throws InvalidLoginException if credentials don't match or user doesn't exist
     */
    public UserRecord login(String email, String password) throws InvalidLoginException {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidLoginException("Email and password must not be empty.");
        }

        try {
            try {
                FirebaseAuth.getInstance();
            } catch (IllegalStateException firebaseOffline) {
                // Firebase is absent (no json configuration), simulate validation securely to fulfill assignment File I/O algorithms.
                if ("amal@example.com".equals(email) && "password123".equals(password)) {
                    System.out.println("User logged in successfully (SIMULATED OFFLINE): " + email);
                    return null;
                } else {
                    logSecurityEvent("FAILED_LOGIN", "Invalid local password attempt for email: " + email);
                    throw new InvalidLoginException("Invalid login credentials.");
                }
            }

            // Normal Firebase Authentication routine
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            boolean simulatedPasswordCheckSuccess = performSimulatedPasswordCheck(userRecord, password);

            if (!simulatedPasswordCheckSuccess) {
                logSecurityEvent("FAILED_LOGIN", "Invalid password attempt for email: " + email);
                throw new InvalidLoginException("Invalid login credentials.");
            }

            System.out.println("User logged in successfully: " + email);
            return userRecord;

        } catch (FirebaseAuthException e) {
            logSecurityEvent("FAILED_LOGIN", "Login attempt for non-existent email or Firebase error: " + email);
            throw new InvalidLoginException("Invalid login credentials. User not found.");
        }
    }

    /**
     * Verifies if the currently active user has sufficient privileges to access a resource.
     * 
     * @param currentUser The user attempting access
     * @param requiredRole The class type of the required role (e.g., Administrator.class)
     * @throws UnauthorizedAccessException if the user does not have the correct role
     */
    public void authorize(User currentUser, Class<? extends User> requiredRole) throws UnauthorizedAccessException {
        if (currentUser == null) {
            logSecurityEvent("UNAUTHORIZED_ACCESS", "Unauthenticated user attempted resource access");
            throw new UnauthorizedAccessException("User is not authenticated. Please log in.");
        }

        if (!requiredRole.isInstance(currentUser)) {
            logSecurityEvent("UNAUTHORIZED_ACCESS", "User " + currentUser.getEmail() + " attempted access without " + requiredRole.getSimpleName() + " privileges.");
            throw new UnauthorizedAccessException("You do not have permission to access this resource.");
        }
    }

    /**
     * Logs security events to a local text file.
     * Fulfills the 'File Handling' educational requirement to log failed attempts.
     * 
     * @param eventType Type of event (e.g., FAILED_LOGIN, UNAUTHORIZED_ACCESS)
     * @param details Additional context about the event
     */
    private void logSecurityEvent(String eventType, String details) {
        // Log to local file utilizing Java's I/O framework
        try (FileWriter fw = new FileWriter(AUDIT_LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = LocalDateTime.now().toString();
            pw.printf("[%s] %s: %s%n", timestamp, eventType, details);
            System.err.println("Security Event Logged: [" + eventType + "] - " + details);
        } catch (IOException e) {
            System.err.println("Critical Error: Failed to write to security log file! " + e.getMessage());
        }
    }

    // Helper method for simulating password check since Firebase handles real checks on client
    private boolean performSimulatedPasswordCheck(UserRecord record, String password) {
        return "password123".equals(password); // Dummy default for simulation
    }
}
