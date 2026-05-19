package com.superx.Dao;

// Imports needed for Firebase Admin SDK Storage
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket; // Use Bucket from Admin SDK
import com.google.firebase.cloud.StorageClient; // Use StorageClient from Admin SDK

import java.io.File;
import java.io.IOException; // Add IOException
import java.nio.file.Files;
import java.nio.file.Path; // Import Path

public class FirebaseImage {

    // No need for BUCKET_NAME constant here if using the default bucket configured in FireBaseInitializer

    /**
     * Uploads a local file to the default Firebase Storage bucket using the initialized Admin SDK.
     *
     * @param localFilePath            The absolute path to the local file to upload.
     * @param destinationPathInStorage The desired path and filename in Firebase Storage (e.g., "coupon_screenshots/image.jpg").
     * @return The public URL of the uploaded file, or null if the upload fails.
     */
    /**
     * Uploads a local file to the default Firebase Storage bucket using the initialized Admin SDK.
     * This version uses the create(String, byte[], BlobTargetOption...) method signature.
     *
     * @param localFilePath            The absolute path to the local file to upload.
     * @param destinationPathInStorage The desired path and filename in Firebase Storage (e.g., "coupon_screenshots/image.jpg").
     * @return The public URL of the uploaded file, or null if the upload fails.
     */
    public static String uploadImage(String localFilePath, String destinationPathInStorage) {
        System.out.println("DEBUG: Attempting to upload image (using String path method)...");
        System.out.println("DEBUG: Local file path: [" + localFilePath + "]");
        System.out.println("DEBUG: Destination path: [" + destinationPathInStorage + "]");

        try {
            // --- Validate Input File ---
            File file = new File(localFilePath);
            if (!file.exists() || !file.isFile()) {
                System.err.println("ERROR: File does not exist or is not a file: " + localFilePath);
                return null;
            }
            if (file.length() == 0) {
                 System.err.println("ERROR: File is empty: " + localFilePath);
                 return null;
            }
            Path filePath = file.toPath();

            // --- Get Storage Bucket Instance ---
            Bucket bucket = StorageClient.getInstance().bucket();
            if (bucket == null) {
                 System.err.println("FATAL ERROR: Storage bucket is null. Check FireBaseInitializer logs.");
                 return null;
            }
            String bucketName = bucket.getName();
            System.out.println("DEBUG: Obtained storage bucket: " + bucketName);

            // --- Determine Content Type ---
            String contentType = null;
            try {
                 contentType = Files.probeContentType(filePath);
                 System.out.println("DEBUG: Probed content type: " + contentType);
            } catch (IOException ioEx) {
                 System.err.println("DEBUG: Warning - Could not probe content type. Error: " + ioEx.getMessage());
            }
            if (contentType == null) { // Fallback guessing
                String lowerCaseName = destinationPathInStorage.toLowerCase();
                if (lowerCaseName.endsWith(".png")) contentType = "image/png";
                else if (lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".jpeg")) contentType = "image/jpeg";
                else if (lowerCaseName.endsWith(".gif")) contentType = "image/gif";
                else contentType = "application/octet-stream";
                System.out.println("DEBUG: Guessed content type: " + contentType);
            }

            // --- Upload using create(String, byte[], BlobTargetOption...) ---
            System.out.println("DEBUG: Starting upload to bucket using String path...");
            byte[] fileBytes = Files.readAllBytes(filePath);

            // Use the destinationPathInStorage (String) directly as the first argument
            // Pass the contentType explicitly using BlobTargetOption.contentType()
            // *** Make sure Bucket.BlobTargetOption is imported correctly ***
           bucket.create(destinationPathInStorage, fileBytes, contentType);

            System.out.println("DEBUG: File uploaded successfully to Storage: " + destinationPathInStorage + " ✅");
            // --- End of specific change ---

            // --- Make File Publicly Readable ---
            BlobId blobId = BlobId.of(bucketName, destinationPathInStorage); // Still need BlobId for ACL
            /*try {
                bucket.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
                System.out.println("DEBUG: Public read ACL set successfully for: " + destinationPathInStorage);
            } catch (Exception aclEx) {
                 System.err.println("ERROR: Failed to set public read ACL for " + destinationPathInStorage);
                 aclEx.printStackTrace();
            }*/

          String encodedPath =
        java.net.URLEncoder.encode(destinationPathInStorage, "UTF-8");

String publicUrl =
        "https://firebasestorage.googleapis.com/v0/b/"
        + bucketName
        + "/o/"
        + encodedPath
        + "?alt=media";

System.out.println("DEBUG: Constructed Public URL: " + publicUrl);

return publicUrl;

        } catch (IOException e) { // Catch specific IO exceptions
             System.err.println("--- IO ERROR UPLOADING IMAGE ---");
             e.printStackTrace();
             return null;
        } catch (Exception e) { // Catch other potential errors
            System.err.println("--- GENERAL ERROR UPLOADING IMAGE ---");
            e.printStackTrace();
            return null;
        }
    }
}