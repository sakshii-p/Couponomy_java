package com.superx.Controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import com.superx.Model.UserDetails;
import com.superx.Model.UserSession;

public class LoginController {

    private String API_KEY;
    private String PROJECT_ID;

    public LoginController() {

        try {

            Properties prop = new Properties();

            InputStream input = getClass()
                    .getClassLoader()
                    .getResourceAsStream("config.properties");

            prop.load(input);

            API_KEY = prop.getProperty("API_KEY");
            PROJECT_ID = prop.getProperty("PROJECT_ID");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String userLogin(String email, String password) {

        String link =
                "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                        + API_KEY;

        String payload = String.format(
                "{\n" +
                "    \"email\": \"%s\",\n" +
                "    \"password\": \"%s\",\n" +
                "    \"returnSecureToken\": true\n" +
                "}",
                email,
                password
        );

        try {

            URL url = new URL(link);

            HttpsURLConnection connection =
                    (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setRequestProperty(
                    "Content-Type",
                    "application/json");

            connection.setDoOutput(true);

            OutputStream oStream = connection.getOutputStream();

            oStream.write(payload.getBytes());

            if (connection.getResponseCode() == 200) {

                fetchUserData(email);

                if (UserSession.getCurrentUser()
                        .getRole()
                        .equalsIgnoreCase("admin")) {

                    return "admin";

                } else {

                    return "user";
                }

            } else {

                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(
                                        connection.getErrorStream()));

                StringBuffer errorMessage = new StringBuffer();

                String errorLine;

                while ((errorLine = br.readLine()) != null) {
                    errorMessage.append(errorLine);
                }

                br.close();

                JSONObject messageObject =
                        new JSONObject(errorMessage.toString());

                String message =
                        messageObject
                                .getJSONObject("error")
                                .getString("message");

                return message;
            }

        } catch (Exception e) {

            return e.toString();
        }
    }

    private void fetchUserData(String email) {

        try {

            String documentId = email;

            String urlString =
                    "https://firestore.googleapis.com/v1/projects/"
                            + PROJECT_ID
                            + "/databases/(default)/documents/userInfo/"
                            + documentId
                            + "?key="
                            + API_KEY;

            URL url = new URL(urlString);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));

            String inputLine;

            StringBuilder responseBuilder = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                responseBuilder.append(inputLine);
            }

            in.close();

            JSONObject json =
                    new JSONObject(responseBuilder.toString());

            JSONObject fields =
                    json.getJSONObject("fields");

            UserDetails user = new UserDetails();

            user.setUserId(email);

            user.setName(
                    fields.getJSONObject("name")
                            .getString("stringValue"));

            user.setDob(
                    fields.getJSONObject("dob")
                            .getString("stringValue"));

            user.setPhoneNo(
                    fields.getJSONObject("phone")
                            .getString("stringValue"));

            user.setRole(
                    fields.getJSONObject("role")
                            .getString("stringValue"));

            user.setWalletCoins(
                    fields.getJSONObject("walletCoins")
                            .getString("stringValue"));

            UserSession.setCurrentUser(user);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}