package com.superx.View;

import java.time.LocalDate;
import java.time.Period;

import com.superx.Controller.PageManager;
import com.superx.Controller.SignUpController;

import com.superx.Dao.UserRegistrationDetails;

import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RegisterPage {

    Stage registerPageStage;
    Scene registerPageScene;

    public void setRegisterPageStage(Stage registerPageStage) {
        this.registerPageStage = registerPageStage;
    }

    public void setRegisterPageScene(Scene registerPageScene) {
        this.registerPageScene = registerPageScene;
    }

    SignUpController signUpController = new SignUpController();
    UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails();

    public StackPane registerPageHBox() {

        double screenWidth = 1500;
        double screenHeight = 850;

        ImageView backgroundImage = new ImageView(new Image("assets/images/backgroundImage.png"));
        backgroundImage.setFitWidth(screenWidth);
        backgroundImage.setFitHeight(screenHeight);
        backgroundImage.setPreserveRatio(false);

        VBox registerBox = new VBox();
        registerBox.setPadding(new Insets(20));
        registerBox.setSpacing(20);
        registerBox.setPrefWidth(450);
        registerBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-border-width: 1.5;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 20, 0.3, 0, 10);");
        registerBox.setEffect(new javafx.scene.effect.GaussianBlur(50));

        Image logoImage = new Image("assets/images/image.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setPreserveRatio(true);
        logoImageView.setFitHeight(44);

        Label titleLabel = new Label("Create your Couponomy Account!!");
        titleLabel.setFont(Font.font("Tahoma", 18));
        titleLabel.setTextFill(Color.web("#ffee00ff"));

        // Styled Fields
        Label nameLabel = new Label("Name");
        nameLabel.setFont(Font.font("Tahoma", 14));
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.setPrefHeight(42);
        nameField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-font-size: 14px;");

        VBox nameGroup = new VBox(2, nameLabel, nameField);

        // Date of Birth fields
        Label dobLabel = new Label("Date of Birth");
        dobLabel.setFont(Font.font("Tahoma", 14));
        DatePicker dobPicker = new DatePicker();
        dobPicker.setPrefHeight(42);
        dobPicker.setPromptText("DD/MM/YYYY");
        dobPicker.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-font-size: 14px;");
        dobPicker.getEditor().setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                        "-fx-font-size: 14px;");

        VBox dobGroup = new VBox(2, dobLabel, dobPicker);

        // Phone No. Field
        Label phoneLabel = new Label("Phone No");
        phoneLabel.setFont(Font.font("Tahoma", 14));
        ComboBox<String> countryCodeBox = new ComboBox<>();
        countryCodeBox.getItems().addAll("+91", "+1", "+44", "+61", "+81");
        countryCodeBox.setValue("+91");
        countryCodeBox.setPrefHeight(42);
        countryCodeBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 10;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);");
        TextField phoneField = new TextField();
        phoneField.setPromptText("10-digit number");
        phoneField.setPrefHeight(42);
        phoneField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-font-size: 14px;");
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                phoneField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
        HBox phoneInput = new HBox(8, countryCodeBox, phoneField);
        phoneInput.setAlignment(Pos.CENTER_LEFT);

        VBox phoneGroup = new VBox(2, phoneLabel, phoneInput);

        // Email Field
        Label emailLabel = new Label("Email ID");
        emailLabel.setFont(Font.font("Tahoma", 14));
        TextField emialIdField = new TextField();
        emialIdField.setPromptText("Enter your Email ID");
        emialIdField.setPrefHeight(42);
        emialIdField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-font-size: 14px;");

        VBox userIdGroup = new VBox(2, emailLabel, emialIdField);

        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Tahoma", 14));

        // Password Field with Show Pasword Toggle
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(42);
        passwordField.setPrefWidth(310);
        passwordField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-font-size: 14px;");

        TextField visibleTextField = new TextField();
        visibleTextField.setPromptText("Enter your password");
        visibleTextField.setPrefHeight(42);
        visibleTextField.setPrefWidth(310);
        visibleTextField.setStyle(passwordField.getStyle());

        visibleTextField.textProperty().bindBidirectional(passwordField.textProperty());

        visibleTextField.setManaged(false);
        visibleTextField.setVisible(false);

        Button toggleButton = new Button("\uD83D\uDC41");
        toggleButton.setStyle("-fx-background-color: transparent; -fx-font-size: 18px; -fx-cursor: hand;");
        toggleButton.setFocusTraversable(false);

        toggleButton.setOnAction(event -> {
            boolean show = passwordField.isVisible();
            passwordField.setVisible(!show);
            passwordField.setManaged(!show);
            visibleTextField.setVisible(show);
            visibleTextField.setManaged(show);
            toggleButton.setText(show ? "\uD83D\uDC41" : "\uD83D\uDD12");
        });

        StackPane passwordPane = new StackPane(passwordField, visibleTextField);

        HBox inputWithButton = new HBox(passwordPane, toggleButton);
        inputWithButton.setSpacing(4);

        VBox passwordGroup = new VBox(2, passwordLabel, inputWithButton);

        // Confirm Password Field
        Label confirmPasswordLabel = new Label("Confirm Password");
        confirmPasswordLabel.setFont(Font.font("Tahoma", 14));
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        confirmPasswordField.setPrefHeight(42);
        confirmPasswordField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-font-size: 14px;");

        VBox confirmPasswordGroup = new VBox(2, confirmPasswordLabel, confirmPasswordField);

        // Gender Field
        Label genderLabel = new Label("Gender");
        genderLabel.setFont(Font.font("Tahoma", 14));
        RadioButton male = new RadioButton("Male");
        RadioButton female = new RadioButton("Female");
        ToggleGroup genderGroup = new ToggleGroup();
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        male.setFont(Font.font("Tahoma", 14));
        female.setFont(Font.font("Tahoma", 14));
        male.setTextFill(Color.web("#374151"));
        female.setTextFill(Color.web("#374151"));
        HBox genderBox = new HBox(10, male, female);
        genderBox.setAlignment(Pos.CENTER_LEFT);

        VBox genderGroupBox = new VBox(2, genderLabel, genderBox);

        // Register Button
        Button registerButton = new Button("Create an Account");
        registerButton.setPrefHeight(80);
        registerButton.setMaxWidth(300);
        registerButton.setStyle(
                "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 14px;");

        VBox registerBtnBox = new VBox(registerButton);
        registerBtnBox.setAlignment(Pos.CENTER);

        // Success/Fail Labels
        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Tahoma", 14));
        messageLabel.setStyle("-fx-font-weight: bold;");
        Label detailLabel = new Label();
        detailLabel.setStyle("-fx-text-fill: red;");

        VBox messageVBox = new VBox(messageLabel, detailLabel);
        messageVBox.setAlignment(Pos.CENTER);

        // Add all components to registerBox
        registerBox.getChildren().addAll(
                logoImageView, titleLabel,
                nameGroup,
                dobGroup,
                phoneGroup,
                userIdGroup,
                passwordGroup,
                confirmPasswordGroup,
                genderGroupBox,
                registerBtnBox,
                messageVBox);

        // RIGHT container (centered)
        StackPane.setAlignment(registerBox, Pos.CENTER);

        // Registration Logic unchanged
        registerButton.setOnAction(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            // String countryCode = countryCodeBox.getValue();
            String emailId = emialIdField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String dobString;
            LocalDate dob = dobPicker.getValue();

            messageLabel.setText("");
            detailLabel.setText("");

            String gender = "";
            if (genderGroup.getSelectedToggle() != null) {
                gender = ((RadioButton) genderGroup.getSelectedToggle()).getText();
            }

            messageLabel.setText("");
            detailLabel.setText("");

            if (name.isEmpty() || phone.isEmpty() || emailId.isEmpty()
                    || password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setText("❌ Registration Failed");
                detailLabel.setText("All fields are required.");
                return;
            }

            if (phone.length() != 10 || !phone.matches("\\d{10}")) {
                messageLabel.setText("❌ Registration Failed");
                detailLabel.setText("Phone number must be exactly 10 digits.");
                return;
            }

            if (dob == null) {
                messageLabel.setText("❌ Registration Failed");
                detailLabel.setText("Please select your Date of Birth.");
                return;
            } else
                dobString = dob.toString();

            int age = calculateAge(dob);
            if (age < 18) {
                messageLabel.setText("❌ Registration Failed");
                detailLabel.setText("You must be at least 18 years old to register.");
                return;
            }

            if (!isStrongPassword(password)) {
                messageLabel.setText("❌ Registration Failed");
                detailLabel.setText(
                        "Password must be at least 8 characters and include a letter, number, and special character.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                messageLabel.setText("❌ Registration Failed");
                detailLabel.setText("Passwords do not match.");
                return;

            }

            String error = signUpController.registerNewUser(name, phone, emailId, confirmPassword, dobString, gender);

            if (error.equals("true")) {
                messageLabel.setText("");
                detailLabel.setText("");

                // Account Created Alert
                Alert alert = new Alert(AlertType.INFORMATION,
                        "✅ Your account has been created successfully!", ButtonType.NEXT);
                alert.setTitle("Couponomy");
                alert.setHeaderText("Welcome to Couponomy!");

                alert.getDialogPane().setPrefSize(500, 300);

                alert.getDialogPane().setStyle(
                        "-fx-font-size: 17px;" +
                                "-fx-background-color: linear-gradient(to right,#e0f2fe,#c7d2fe);" +
                                "-fx-border-radius: 12;" +
                                "-fx-background-radius: 12;");

                Node headerText = alert.getDialogPane().lookup(".header-panel");
                if (headerText != null) {
                    headerText.setStyle("-fx-font-size: 22px; -fx-text-fill: #3b82f6; -fx-font-weight: bold;");
                }

                alert.showAndWait();

                // After OK pressed
                PageManager.getInstance().showLoginPage();
            } else {

                if (error.equals("EMAIL_EXISTS")) {
                    messageLabel.setText("❌ Registered Failed!");
                    detailLabel.setText("Email Already exists...!!!");

                } else if (error.equals("INVALID_EMAIL")) {
                    messageLabel.setText("❌ Registered Failed!");
                    detailLabel.setText("Invalid Email...!!!");

                } else if (error.equals("Server Down")) {
                    messageLabel.setText("❌ Registered Failed!");
                    detailLabel.setText("Server Down Sorry...!!!");

                } else {
                    messageLabel.setText("❌ Registered Failed!");
                    detailLabel.setText("Something went wrong please try again");
                }
            }
        });
        HBox registerWrapper = new HBox();
        registerWrapper.setPadding(new Insets(100, 100, 100, 100));
        registerWrapper.setAlignment(Pos.CENTER_RIGHT);
        registerWrapper.setPrefWidth(screenWidth);
        registerWrapper.setPrefHeight(screenHeight);
        registerWrapper.getChildren().add(registerBox);

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImage, registerWrapper);
        return root;
    }

    private int calculateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    private boolean isStrongPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }
}
