package com.superx.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class HelpSupportSection {

    public VBox getPage() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #F8F8FF;");

        // Page Title
        Text title = new Text("Help & Support");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setFill(Color.web("#4B0082"));

        // Emergency Support Banner
        VBox emergencyBox = createEmergencyBanner();

        // Quick Support Options (Chat, Email, Call)
        HBox quickSupport = createQuickSupportOptions();

        // Popular Help Topics
        VBox topicsBox = createPopularTopics();

        // Buttons for Full Pages
        HBox navButtons = new HBox(15,
                createNavButton("View FAQs", "#4B0082"),
                createNavButton("Report an Issue", "#B22222"));
        navButtons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, emergencyBox, quickSupport, topicsBox, navButtons);
        return root;
    }

    // Emergency Banner
    private VBox createEmergencyBanner() {
        Text alertText = new Text("⚠ Urgent Issue? Contact us immediately!");
        alertText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        alertText.setFill(Color.WHITE);

        VBox banner = new VBox(alertText);
        banner.setAlignment(Pos.CENTER);
        banner.setPadding(new Insets(12));
        banner.setStyle("-fx-background-color: #B22222; -fx-background-radius: 8;");
        return banner;
    }

    // Quick Support Options (3 Buttons)
    private HBox createQuickSupportOptions() {
        HBox box = new HBox(15,
                createQuickSupportCard("💬 Live Chat", "Chat with our team"),
                createQuickSupportCard("📧 Email", "support@couponmy.com"),
                createQuickSupportCard("📞 Call Us", "+91 99999 99999"));
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private VBox createQuickSupportCard(String title, String subtitle) {
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        titleText.setFill(Color.web("#4B0082"));

        Text subText = new Text(subtitle);
        subText.setFont(Font.font("Segoe UI", 13));
        subText.setFill(Color.web("#555"));

        VBox card = new VBox(3, titleText, subText);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setPrefSize(150, 60);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8;");

        DropShadow shadow = new DropShadow(5, Color.rgb(0, 0, 0, 0.15));
        card.setEffect(shadow);

        card.setCursor(Cursor.HAND);
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #F0F0FF; -fx-background-radius: 8;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 8;"));
        return card;
    }

    // Popular Help Topics (Small Cards)
    private VBox createPopularTopics() {
        Text title = new Text("Popular Topics");
        title.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));
        title.setFill(Color.web("#333"));

        FlowPane topicsPane = new FlowPane(15, 15);
        topicsPane.setPadding(new Insets(5));
        topicsPane.getChildren().addAll(
                createTopicCard("How to use a purchased coupon?"),
                createTopicCard("How to sell my coupon?"),
                createTopicCard("Refund Policy"),
                createTopicCard("Admin Verification Process"),
                createTopicCard("What if coupon is expired?"));

        VBox box = new VBox(8, title, topicsPane);
        return box;
    }

    private VBox createTopicCard(String text) {
        Text topicText = new Text(text);
        topicText.setFont(Font.font("Segoe UI", 14));
        topicText.setFill(Color.web("#4B0082"));

        VBox card = new VBox(topicText);
        card.setPadding(new Insets(8));
        card.setStyle("-fx-background-color: #f9f9ff; -fx-background-radius: 6;");
        card.setCursor(Cursor.HAND);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #ececff; -fx-background-radius: 6;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #f9f9ff; -fx-background-radius: 6;"));
        return card;
    }

    // Navigation Buttons
    private Button createNavButton(String text, String color) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        btn.setTextFill(Color.WHITE);
        btn.setCursor(Cursor.HAND);
        btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8;");
        btn.setOnMouseEntered(
                e -> btn.setStyle("-fx-background-color: derive(" + color + ", 20%); -fx-background-radius: 8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8;"));
        btn.setOnAction(e -> System.out.println("Navigating to: " + text));
        return btn;
    }
}
