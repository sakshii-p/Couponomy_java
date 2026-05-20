package com.superx.Dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

public class UserRegistrationDetails {

    private String API_KEY;
    private String PROJECT_ID;
    private String DOC_ID;

    public UserRegistrationDetails() {

        try {

            Properties prop = new Properties();

            InputStream input = getClass()
                    .getClassLoader()
                    .getResourceAsStream("config.properties");

            prop.load(input);

            API_KEY = prop.getProperty("API_KEY");
            PROJECT_ID = prop.getProperty("PROJECT_ID");
            DOC_ID = prop.getProperty("DOC_ID");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int addUserRegistrationDetails(String name,
                                          String phone,
                                          String email,
                                          String dob,
                                          String gender) {

        DOC_ID = email;

        String link = "https://firestore.googleapis.com/v1/projects/"
                + PROJECT_ID
                + "/databases/(default)/documents/userInfo?documentId="
                + DOC_ID
                + "&key="
                + API_KEY;

        System.out.println(link);

        try {

            URL url = new URL(link);

            HttpsURLConnection connection =
                    (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setRequestProperty(
                    "Content-type",
                    "application/json");

            connection.setDoOutput(true);

            String payload = String.format(
                    "{\n" +
                    "    \"fields\": {\n" +
                    "        \"name\": {\"stringValue\": \"%s\"},\n" +
                    "        \"phone\": {\"stringValue\": \"%s\"},\n" +
                    "        \"emailId\": {\"stringValue\": \"%s\"},\n" +
                    "        \"dob\": {\"stringValue\": \"%s\"},\n" +
                    "        \"gender\": {\"stringValue\": \"%s\"},\n" +
                    "        \"role\": {\"stringValue\": \"user\"},\n" +
                    "        \"walletCoins\": {\"stringValue\": \"1000\"}\n" +
                    "    }\n" +
                    "}",
                    name,
                    phone,
                    email,
                    dob,
                    gender
            );

            OutputStream oStream = connection.getOutputStream();

            oStream.write(payload.getBytes());

            int responseCode = connection.getResponseCode();

            oStream.close();

            connection.disconnect();

            return responseCode;

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }
}