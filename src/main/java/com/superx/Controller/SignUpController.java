package com.superx.Controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import com.superx.Dao.UserRegistrationDetails;

public class SignUpController {

    private String API_KEY;

    public SignUpController() {

        try {

            Properties prop = new Properties();

            InputStream input = getClass()
                    .getClassLoader()
                    .getResourceAsStream("config.properties");

            prop.load(input);

            API_KEY = prop.getProperty("API_KEY");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String registerNewUser(String name,
                                  String phone,
                                  String email,
                                  String password,
                                  String dob,
                                  String gender) {

        String signUpLink =
                "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="
                        + API_KEY;

        try {

            URL url = new URL(signUpLink);

            HttpsURLConnection connection =
                    (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setRequestProperty(
                    "Content-Type",
                    "application/json");

            connection.setDoOutput(true);

            String payload = String.format(
                    "{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}",
                    email,
                    password
            );

            OutputStream oStream = connection.getOutputStream();

            oStream.write(payload.getBytes());

            if (connection.getResponseCode() == 200) {

                UserRegistrationDetails userRegistrationDetails =
                        new UserRegistrationDetails();

                if (userRegistrationDetails.addUserRegistrationDetails(
                        name,
                        phone,
                        email,
                        dob,
                        gender) == 200)

                    return "true";

                else
                    return "Server Down";

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

                JSONObject errorObject =
                        new JSONObject(errorMessage.toString());

                String message =
                        errorObject
                                .getJSONObject("error")
                                .getString("message");

                return message;
            }

        } catch (Exception e) {

            return e.toString();
        }
    }
}