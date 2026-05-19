package com.superx.Configuration;

import java.io.InputStream; // Use InputStream instead of FileInputStream
import java.io.IOException;
import java.io.FileNotFoundException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.superx.Dao.CouponInfoDAO;

public class FireBaseInitializer {

    // Static initializer block runs when the class is first loaded.
    static {
        try {
            System.out.println("DEBUG: FireBaseInitializer static block START");
            initializerFireBase();
            System.out.println("DEBUG: FireBaseInitializer static block END - SUCCESS ✅");
        } catch (IOException e) {
            // Log the error that occurred during IO operations (file reading, network)
            System.err.println("DEBUG: FATAL IO ERROR in FireBaseInitializer static block! ❌");
            e.printStackTrace();
        } catch (RuntimeException e) {
            // Log other critical runtime errors during setup
            System.err.println("DEBUG: FATAL RUNTIME ERROR during Firebase initialization! ❌");
            e.printStackTrace();
        } catch (Exception e) {
             // Catch any other unexpected exceptions during startup
             System.err.println("DEBUG: UNEXPECTED FATAL ERROR during Firebase initialization! ❌");
             e.printStackTrace();
        }
    }

    // Method to initialize Firebase
    private static void initializerFireBase() throws IOException, RuntimeException {
        System.out.println("DEBUG: initializerFireBase START");
        InputStream serviceAccountStream = null; // Use InputStream for classpath resources

        // --- Step 1: Load the Service Account Key from Classpath ---
        try {
            // Load the resource relative to the 'resources' folder root
            String resourcePath = "firebasePrivateKey.json";
            serviceAccountStream = FireBaseInitializer.class.getClassLoader().getResourceAsStream(resourcePath);

            if (serviceAccountStream == null) {
                // Critical error if the file is not found in the classpath
                System.err.println("DEBUG: FATAL ERROR - " + resourcePath + " NOT FOUND in classpath resources! ❌");
                System.err.println("DEBUG: Make sure the file exists in 'src/main/resources/' and the project build includes resources.");
                throw new FileNotFoundException(resourcePath + " not found in classpath resources.");
            }
            System.out.println("DEBUG: InputStream created successfully for key file from classpath. 👍");

        } catch (FileNotFoundException e) {
             System.err.println("DEBUG: Caught FileNotFoundException during key file loading."); // Already logged above
             e.printStackTrace();
             throw e; // Stop initialization
        } catch (Exception e) {
             System.err.println("DEBUG: UNEXPECTED ERROR loading service account key stream. 😟");
             e.printStackTrace();
             // Wrap in IOException to match method signature, indicating a setup failure
             throw new IOException("Failed to load service account key stream.", e);
        }


        // --- Step 2: Define Project URLs ---
        // !!! IMPORTANT: Make sure these are the correct URLs for YOUR NEW project !!!
        String databaseUrl = "https://couponomy-124bd-default-rtdb.firebaseio.com"; // Your RTDB URL
        String storageBucket = "couponomy-124bd.firebasestorage.app"; // Your Storage Bucket URL

        System.out.println("DEBUG: Using DatabaseURL: " + databaseUrl);
        System.out.println("DEBUG: Using StorageBucket: " + storageBucket);

        // --- Step 3: Build Firebase Options ---
        FirebaseOptions options_Cpm = null;
        try {
            // Use the InputStream obtained from the classpath resource
            options_Cpm = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .setDatabaseUrl(databaseUrl) // Essential for Admin SDK features
                    .setStorageBucket(storageBucket) // Essential for Admin SDK Storage access
                    .build();
            System.out.println("DEBUG: FirebaseOptions built successfully. 👍");
        } catch (IOException e) {
            System.err.println("DEBUG: ERROR building FirebaseOptions. Invalid credentials stream or incorrect URLs? 😟");
            e.printStackTrace();
            throw e; // Stop if options fail
        } finally {
            // Ensure the InputStream is closed after reading credentials
            if (serviceAccountStream != null) {
                try {
                    serviceAccountStream.close();
                    System.out.println("DEBUG: Service account InputStream closed.");
                } catch (IOException e) {
                    System.err.println("DEBUG: Warning - Could not close service account stream. May cause resource leak.");
                    e.printStackTrace(); // Log but don't stop execution here
                }
            }
        }


        // --- Step 4: Initialize FirebaseApp (check if already initialized) ---
        try {
            // Check if the default app already exists
             boolean appExists = false;
             for (FirebaseApp existingApp : FirebaseApp.getApps()) {
                 if (existingApp.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                     appExists = true;
                     System.out.println("DEBUG: Found existing default FirebaseApp.");
                     break;
                 }
             }

            // Initialize only if the default app doesn't exist
            if (!appExists) {
                FirebaseApp.initializeApp(options_Cpm);
                System.out.println("DEBUG: FirebaseApp initialized successfully. 🎉");
            } else {
                System.out.println("DEBUG: FirebaseApp was already initialized. Skipping initialization.");
            }
        } catch (IllegalStateException e) {
             // This can happen if initialization is attempted multiple times with conflicting options
             System.err.println("DEBUG: ERROR initializing FirebaseApp - IllegalStateException. Already initialized with different options? 🤔");
             e.printStackTrace();
             // Decide if this is fatal. If the app exists, maybe it's okay.
             // If you are SURE initialization only happens once, this shouldn't occur.
        } catch (Exception e) {
             System.err.println("DEBUG: UNEXPECTED ERROR initializing FirebaseApp. ❌");
             e.printStackTrace();
             throw new IOException("Failed to initialize FirebaseApp.", e); // Treat as fatal setup error
        }


        // --- Step 5: Get Firestore instance and assign it ---
        Firestore db = null;
        try {
            // Get the Firestore instance associated with the (now hopefully initialized) default FirebaseApp
            db = FirestoreClient.getFirestore();
             if (db != null) {
                 System.out.println("DEBUG: Firestore instance obtained successfully. 👍");
                 // Assign the valid Firestore instance to the DAO's static variable
                 CouponInfoDAO.couponDatabase = db;
                 System.out.println("DEBUG: CouponInfoDAO.couponDatabase ASSIGNED successfully. ✅");
             } else {
                 // If db is null here, something went wrong badly, even if initializeApp didn't throw error.
                 System.err.println("DEBUG: FATAL ERROR - FirestoreClient.getFirestore() returned NULL! Initialization incomplete? ❌");
                 throw new RuntimeException("Failed to get Firestore instance after Firebase initialization was attempted."); // Halt execution
             }
        } catch (Exception e) {
             System.err.println("DEBUG: ERROR obtaining Firestore instance or assigning to DAO. 😟");
             e.printStackTrace();
             // Treat this as a critical failure during startup
             throw new RuntimeException("Failed to obtain/assign Firestore instance.", e);
        }

        System.out.println("DEBUG: initializerFireBase END - SUCCESS ✅");
    }
}