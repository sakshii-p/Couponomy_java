package com.superx.View;

import com.superx.Controller.PageManager;
import com.superx.Controller.StatsManager;
import com.superx.Model.UserDetails;
import com.superx.Model.UserSession;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ProfileSection {

    public VBox getPage() {
        // Root with Gradient Background
        VBox root = new VBox();

        root.setPrefHeight(600);
        root.setFillWidth(true);

        // Spacer at Top (pushes card slightly down)
        Region topSpacer = new Region();
        topSpacer.setPrefHeight(60);

        // Profile Card (vertical layout)
        HBox card = createProfileCard();

        // Action Buttons (Change Password + Logout)
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.setPadding(new Insets(20));
        Button changePasswordBtn = createActionButton("Change Password", "#4B0082");
        Button logoutBtn = createActionButton("Logout", "#B22222");
        actionButtons.getChildren().addAll(changePasswordBtn, logoutBtn);
        logoutBtn.setOnAction(event -> {
            PageManager.getInstance().showLoginPage();
        });

        // Spacer (Push Footer Down)
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Footer (Settings | Help)
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        Text footerText = new Text("© 2025 Coupon App |");
        footerText.setFont(Font.font("Segoe UI", 13));
        footerText.setFill(Color.web("#555"));
        Text settingsLink = createFooterLink("Settings");
        Text helpLink = createFooterLink("Help");
        footer.getChildren().addAll(footerText, settingsLink, new Text("|"), helpLink);

        // Add All to Root
        root.getChildren().addAll(topSpacer, card, actionButtons, spacer, footer);

        return root;
    }

    // Profile Card (Vertical Layout)
    private HBox createProfileCard() {
        UserDetails user = UserSession.getCurrentUser(); // Get current logged-in user

        HBox card = new HBox();
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-border-radius: 18;");
        card.setMaxWidth(720);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        shadow.setColor(Color.rgb(0, 0, 0, 0.15));
        card.setEffect(shadow);

        VBox column = new VBox(12);
        column.setAlignment(Pos.CENTER);

        // Profile Picture
        ImageView profilePic = new ImageView(new Image("assets/images/couponbackground.png", 140, 140, true, true));
        Circle clip = new Circle(70, 70, 70);
        profilePic.setClip(clip);
        profilePic.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.2, 0, 2);");

        // User Name
        Text userName = new Text(user != null ? user.getName() : "Guest");
        userName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        userName.setFill(Color.web("#4B0082"));

        // Personal Info (dynamically filled)
        VBox personalInfo = new VBox(10);
        personalInfo.setAlignment(Pos.CENTER);
        personalInfo.setPadding(new Insets(10));
        personalInfo.setStyle(
                "-fx-background-color: #f9f9ff; -fx-background-radius: 12; -fx-border-color: #e0e0f0; -fx-border-radius: 12;");
        personalInfo.getChildren().addAll(
                createInfoLine("Email", user != null ? user.getUserId() : "-"),
                createInfoLine("Phone", user != null ? user.getPhoneNo() : "-"),
                createInfoLine("DOB", user != null ? user.getDob() : "-"),
                createInfoLine("Member Since", "July 2024") // hardcoded or store join date in DB
        );

        // Stats
        // Stats
        HBox statsRow = new HBox(15);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.setPadding(new Insets(12));

        int purchasedCount = 0;
        int soldCount = 0;

        try {
            purchasedCount = StatsManager.getPurchasedCouponCount(user.getUserId());
            soldCount = StatsManager.getSoldCouponCount(user.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        statsRow.getChildren().addAll(
                createWideStatBox("Wallet", user != null ? user.getWalletCoins() + " Coins" : "0 Coins", "#E6E6FA"),
                createWideStatBox("Purchased Coupons", String.valueOf(purchasedCount), "#FFF0F5"),
                createWideStatBox("Sold Coupons", String.valueOf(soldCount), "#F0F8FF"));

        column.getChildren().addAll(profilePic, userName, personalInfo, statsRow);
        card.getChildren().add(column);

        return card;
    }

    // Personal Info Line
    private HBox createInfoLine(String label, String value) {
        Text labelText = new Text(label + ": ");
        labelText.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        labelText.setFill(Color.web("#555"));

        Text valueText = new Text(value);
        valueText.setFont(Font.font("Segoe UI", 15));
        valueText.setFill(Color.web("#222"));

        HBox line = new HBox(5, labelText, valueText);
        line.setAlignment(Pos.CENTER);
        return line;
    }

    // Wide Stats Box
    private VBox createWideStatBox(String title, String value, String bgColor) {
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        titleText.setFill(Color.web("#555"));

        Text valueText = new Text(value);
        valueText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        valueText.setFill(Color.web("#4B0082"));

        VBox box = new VBox(3, titleText, valueText);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(130);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: " + bgColor
                + "; -fx-background-radius: 12; -fx-border-color: #ddd; -fx-border-radius: 12;");
        return box;
    }

    // Action Buttons
    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8;");
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(
                e -> button.setStyle("-fx-background-color: derive(" + color + ", 20%); -fx-background-radius: 8;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8;"));
        return button;
    }

    // Footer Links
    private Text createFooterLink(String text) {
        Text link = new Text(text);
        link.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        link.setFill(Color.web("#4B0082"));
        link.setUnderline(true);
        link.setCursor(Cursor.HAND);

        link.setOnMouseEntered(e -> link.setFill(Color.web("#6A0DAD")));
        link.setOnMouseExited(e -> link.setFill(Color.web("#4B0082")));
        return link;
    }
}
