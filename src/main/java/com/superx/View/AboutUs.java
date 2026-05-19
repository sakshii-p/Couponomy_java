package com.superx.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AboutUs {

    public BorderPane showAboutUs() {

        BorderPane pane = new BorderPane();

        // ================= MAIN HEADING =================

        Label mainHeading = new Label("About Couponomy");
        mainHeading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        mainHeading.setTextFill(Color.WHITE);

        Label subHeading = new Label("Smart Marketplace for UPI-Based Coupons");
        subHeading.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 20));
        subHeading.setTextFill(Color.web("#eeeeee"));

        // ================= INTRODUCTION =================

        Text introText = new Text(
                "Couponomy is a JavaFX-based desktop application designed to reduce the wastage of unused UPI coupons. "
                        +
                        "The platform allows users to upload unwanted coupons and exchange them using a secure virtual coin-based system.\n\n"
                        +
                        "The main objective of Couponomy is to create a useful ecosystem where digital coupons can be reused instead of being wasted."
        );

        introText.setFont(Font.font("Georgia", FontWeight.SEMI_BOLD, 17));
        introText.setFill(Color.web("#333333"));

        TextFlow introFlow = new TextFlow(introText);
        introFlow.setMaxWidth(850);
        introFlow.setStyle("""
                    -fx-background-color: rgba(255,255,255,0.92);
                    -fx-padding: 30;
                    -fx-background-radius: 18;
                    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 12, 0, 0, 5);
                """);

        // ================= FEATURES =================

        Text featuresText = new Text(
                "Key Features:\n\n"
                        +
                        "• User Authentication using Firebase\n"
                        +
                        "• Upload and exchange unused UPI coupons\n"
                        +
                        "• Virtual wallet coin system\n"
                        +
                        "• Admin verification before public listing\n"
                        +
                        "• Coupon image uploads using Firebase Storage\n"
                        +
                        "• Firestore & Realtime Database integration\n"
                        +
                        "• Modern JavaFX desktop interface\n"
                        +
                        "• Structured MVC architecture"
        );

        featuresText.setFont(Font.font("Georgia", FontWeight.SEMI_BOLD, 17));
        featuresText.setFill(Color.web("#333333"));

        TextFlow featuresFlow = new TextFlow(featuresText);
        featuresFlow.setMaxWidth(850);
        featuresFlow.setStyle("""
                    -fx-background-color: rgba(255,255,255,0.92);
                    -fx-padding: 30;
                    -fx-background-radius: 18;
                    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 12, 0, 0, 5);
                """);

        // ================= WORKFLOW =================

        Text workflowText = new Text(
                "How Couponomy Works:\n\n"
                        +
                        "1. Users upload unused coupons.\n"
                        +
                        "2. Coupon screenshots and details are securely stored.\n"
                        +
                        "3. Admin verifies uploaded coupons.\n"
                        +
                        "4. Approved coupons become visible to all users.\n"
                        +
                        "5. Users can purchase coupons using virtual wallet coins."
        );

        workflowText.setFont(Font.font("Georgia", FontWeight.SEMI_BOLD, 17));
        workflowText.setFill(Color.web("#333333"));

        TextFlow workflowFlow = new TextFlow(workflowText);
        workflowFlow.setMaxWidth(850);
        workflowFlow.setStyle("""
                    -fx-background-color: rgba(255,255,255,0.92);
                    -fx-padding: 30;
                    -fx-background-radius: 18;
                    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 12, 0, 0, 5);
                """);

        // ================= SECURITY SECTION =================

        Text securityText = new Text(
                "Security & Verification:\n\n"
                        +
                        "To maintain authenticity and trust, uploaded coupons are reviewed by the admin before becoming publicly visible. "
                        +
                        "This verification process helps reduce invalid or misleading coupon listings and improves overall platform reliability."
        );

        securityText.setFont(Font.font("Georgia", FontWeight.SEMI_BOLD, 17));
        securityText.setFill(Color.web("#333333"));

        TextFlow securityFlow = new TextFlow(securityText);
        securityFlow.setMaxWidth(850);
        securityFlow.setStyle("""
                    -fx-background-color: rgba(255,255,255,0.92);
                    -fx-padding: 30;
                    -fx-background-radius: 18;
                    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 12, 0, 0, 5);
                """);

        // ================= TECHNOLOGIES =================

        Text techText = new Text(
                "Technologies Used:\n\n"
                        +
                        "• Java\n"
                        +
                        "• JavaFX\n"
                        +
                        "• Firebase Authentication\n"
                        +
                        "• Firebase Firestore\n"
                        +
                        "• Firebase Realtime Database\n"
                        +
                        "• Firebase Storage\n"
                        +
                        "• Maven"
        );

        techText.setFont(Font.font("Georgia", FontWeight.SEMI_BOLD, 17));
        techText.setFill(Color.web("#333333"));

        TextFlow techFlow = new TextFlow(techText);
        techFlow.setMaxWidth(850);
        techFlow.setStyle("""
                    -fx-background-color: rgba(255,255,255,0.92);
                    -fx-padding: 30;
                    -fx-background-radius: 18;
                    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 12, 0, 0, 5);
                """);

        // ================= FOOTER =================

        Label footer = new Label(
                "Couponomy © 2026 | JavaFX Desktop Application"
        );

        footer.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 15));
        footer.setTextFill(Color.web("#f1f1f1"));

        // ================= MAIN CONTAINER =================

        VBox mainContainer = new VBox(
                30,
                mainHeading,
                subHeading,
                introFlow,
                featuresFlow,
                workflowFlow,
                securityFlow,
                techFlow,
                footer
        );

        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(40));

        mainContainer.setStyle("""
                    -fx-background-color: linear-gradient(to bottom, #5f72bd, #9b23ea);
                """);

        // ================= SCROLL PANE =================

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);

        scrollPane.setStyle("""
                    -fx-background: transparent;
                    -fx-background-color: transparent;
                """);

        pane.setCenter(scrollPane);

        return pane;
    }
}