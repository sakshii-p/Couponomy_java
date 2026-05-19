package com.superx.View;

import com.superx.Controller.Initializer;
import com.superx.Controller.LoginController;
import com.superx.Controller.PageManager;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class LoginPage extends Application {

    Stage loginStage;
    Scene loginScene;

    Initializer initializer = new Initializer();
    LoginController loginController = new LoginController();

    @Override
    public void start(Stage primaryStage) {

        PageManager.setMainStage(primaryStage);

        double screenWidth = 1500;
        double screenHeight = 850;

        // Background Image
        ImageView backgroundImage = new ImageView(new Image("assets/images/backgroundImage.png"));
        backgroundImage.setFitWidth(screenWidth);
        backgroundImage.setFitHeight(screenHeight);
        backgroundImage.setPreserveRatio(false);

        // Login Box
        VBox loginBox = new VBox();
        loginBox.setPadding(new Insets(40));
        loginBox.setSpacing(20);
        loginBox.setPrefWidth(420);
        loginBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-border-width: 1.5;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 20, 0.3, 0, 10);");

        loginBox.setEffect(new javafx.scene.effect.GaussianBlur(50));

        Image logoImage = new Image("assets/images/image.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setPreserveRatio(true);
        logoImageView.setFitHeight(44);

        Label subtitle = new Label("Welcome Back!!");
        subtitle.setFont(Font.font("Tahoma", 18));
        subtitle.setTextFill(Color.web("#ffee00ff"));
        Label subtitled = new Label("Please sign in to your Account");
        subtitled.setFont(Font.font("Tahoma", 15));
        subtitled.setTextFill(Color.web("#ffee00ff"));
        VBox subtiltVBox = new VBox(subtitle, subtitled);

        Label emailLabel = new Label("Email Address");
        emailLabel.setFont(Font.font("Tahoma", 14));
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setPrefHeight(42);

        Label passLabel = new Label("Password");
        passLabel.setFont(Font.font("Tahoma", 14));
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        passField.setPrefHeight(42);

        emailField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-font-size: 14px;");

        passField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-font-size: 14px;");

        Label loginerrorLabel = new Label();
        loginerrorLabel.setStyle("-fx-font-weight: bold;" +
                "-fx-text-fill: #ff0000ff;");
        loginerrorLabel.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Log In");
        loginButton.setPrefHeight(44);
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setStyle(
                "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 14px;");
        loginButton.setOnAction(event -> {
            String emailId = emailField.getText();
            String password = passField.getText();

            String message = loginController.userLogin(emailId, password);
            if (message.equals("user")) {
                loginerrorLabel.setText("");
                PageManager.getInstance().showLandingPage();
            } else if (message.equals("admin")) {
                loginerrorLabel.setText("");
                try {
                    AdminPage adminPage = new AdminPage();
                    Stage adminStage = new Stage();
                    adminPage.start(adminStage);

                    // Optional: Hide login window
                    ((Stage) loginButton.getScene().getWindow()).close();

                } catch (Exception e) {
                    e.printStackTrace();
                    loginerrorLabel.setText("Failed to load admin page.");
                }

            } else {
                switch (message) {
                    case "INVALID_LOGIN_CREDENTIALS":
                        loginerrorLabel.setText("Invalid Email or Password");
                        break;
                    case "INVALID_EMAIL":
                        loginerrorLabel.setText("Enter a Valid Email");
                        break;
                    case "MISSING_PASSWORD":
                        loginerrorLabel.setText("Enter password");
                        break;
                    default:
                        loginerrorLabel.setText("Something went wrong. Please try again.");
                }
            }
        });

        HBox orDivider = new HBox();
        orDivider.setAlignment(Pos.CENTER);
        Label orLabel = new Label("Or");
        orLabel.setTextFill(Color.web("#000000ff"));
        Separator leftSep = new Separator();
        Separator rightSep = new Separator();
        HBox.setHgrow(leftSep, Priority.ALWAYS);
        HBox.setHgrow(rightSep, Priority.ALWAYS);
        orDivider.setSpacing(10);
        orDivider.getChildren().addAll(leftSep, orLabel, rightSep);

        Label dontHaveAccount = new Label("Don't have an account?");
        dontHaveAccount.setTextFill(Color.web("#000000ff"));
        dontHaveAccount.setAlignment(Pos.CENTER);
        dontHaveAccount.setMaxWidth(Double.MAX_VALUE);

        Button signUp = new Button("Sign up");
        signUp.setPrefHeight(44);
        signUp.setMaxWidth(Double.MAX_VALUE);
        signUp.setStyle(
                "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 14px;");
        signUp.setOnAction(event -> {
            PageManager.getInstance().showRegisterPage();
        });

        loginBox.getChildren().addAll(logoImageView, subtiltVBox, emailLabel, emailField, passLabel, passField,
                loginerrorLabel,
                loginButton, orDivider, dontHaveAccount, signUp);
        // Wrapper to position loginBox on the right side
        HBox loginWrapper = new HBox();
        loginWrapper.setPadding(new Insets(100, 100, 100, 100));
        loginWrapper.setAlignment(Pos.CENTER_RIGHT);
        loginWrapper.setPrefWidth(screenWidth);
        loginWrapper.setPrefHeight(screenHeight);
        loginWrapper.getChildren().add(loginBox);

        // Final layout with background
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImage, loginWrapper);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setTitle("Couponomy");
        primaryStage.getIcons().add(new Image("assets/images/logoPNG.png"));
        primaryStage.setScene(scene);
        primaryStage.setHeight(850);
        primaryStage.setWidth(1500);
        primaryStage.show();
    }
}
