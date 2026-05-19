package com.superx.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.superx.Controller.PageManager;
import com.superx.Model.CouponDetails;

public class CouponCard {

    // purchased coupon card
    public static VBox buyCouponCard(CouponDetails coupon) {
        String expiresIn = calculateExpiryText(coupon.getExpiry());

        VBox couponBody = createBaseCouponBody();
        couponBody.setSpacing(10);
        couponBody.setPadding(new Insets(12));
        // couponBody.setAlignment(Pos.TOP_CENTER);
        couponBody.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 0, 6);" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 16;");

        // IMAGE
        StackPane imageContainer = buildImageContainer(coupon.getCouponPosterURL(), expiresIn);
        imageContainer.setStyle("-fx-background-radius: 14; -fx-overflow: hidden;");

        // TITLE
        Text titleText = new Text(coupon.getCouponTitle());
        titleText.setFont(Font.font("Tahoma", FontWeight.BOLD, 22));
        titleText.setFill(Color.web("#2E3A59"));
        titleText.setWrappingWidth(220); // better wrapping for long titles

        // BRAND NAME
        Text brandText = new Text(coupon.getBrandName());
        brandText.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 16));
        brandText.setFill(Color.web("#666A7A"));
        HBox brandHBox = new HBox(brandText);
        brandHBox.setAlignment(Pos.CENTER_LEFT);

        // COUPON ID
        Text idText = new Text("ID: " + coupon.getCouponId());
        idText.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        idText.setFill(Color.web("#999999"));
        VBox idtextVbox = new VBox(idText);
        idtextVbox.setAlignment(Pos.CENTER_RIGHT);

        // COUPON CODE (only if purchased)
        VBox codeBox = new VBox();
        if (coupon.getBuyerId() != null && !coupon.getBuyerId().isBlank() && coupon.getCouponCode() != null) {
            Text codeText = new Text("Code: " + coupon.getCouponCode());
            codeText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            codeText.setFill(Color.web("#27AE60"));
            StackPane codeContainer = new StackPane(codeText);
            codeContainer.setStyle(
                    "-fx-border-color: #27AE60;" +
                            "-fx-border-width: 1.5;" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 4 8 4 8;" +
                            "-fx-background-color: rgba(39,174,96,0.08);");
            codeBox.getChildren().add(codeContainer);
            codeBox.setAlignment(Pos.CENTER);
            codeBox.setPadding(new Insets(6, 0, 0, 0));
        }

        couponBody.getChildren().addAll(imageContainer, titleText, brandHBox, idtextVbox, codeBox);

        // Hover effect
        addHoverEffect(couponBody);

        // Navigation to description
        couponBody.setCursor(Cursor.HAND);
        couponBody.setOnMouseClicked(e -> PageManager.getInstance().showCouponDescription(coupon));

        return couponBody;
    }

    // seller coupon card
    // seller coupon card
    public static VBox sellCouponCard(
            String imageUrl,
            String expiryDate, // format: "2025-07-30"
            String title,
            String brandName,
            String couponId,
            String status) {

        // Calculate Remaining Days
        String expiresIn = calculateExpiryText(expiryDate);

        // Coupon Body
        VBox couponBody = new VBox(10);
        // couponBody.setAlignment(Pos.TOP_CENTER);
        couponBody.setPadding(new Insets(12));
        couponBody.setPrefWidth(220);
        couponBody.setPrefHeight(270);
        couponBody.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-width: 1.2;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 0, 6);");

        // Shadow
        DropShadow shadow = new DropShadow();
        shadow.setRadius(6);
        shadow.setOffsetY(2);
        shadow.setColor(Color.rgb(0, 0, 0, 0.12));
        couponBody.setEffect(shadow);

        // IMAGE with "Expires In" Badge
        StackPane imageContainer = new StackPane();
        imageContainer.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView(new Image(imageUrl));
        imageView.setFitWidth(200);
        imageView.setFitHeight(130);
        imageView.setSmooth(true);
        imageView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(18);
        clip.setArcHeight(18);
        imageView.setClip(clip);
        imageContainer.getChildren().add(imageView);

        if (!expiresIn.isEmpty()) {
            Text expiresText = new Text(expiresIn);
            expiresText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
            expiresText.setFill(Color.WHITE);

            String badgeColor = expiresIn.equals("Expired") ? "#E53935" : "#FF9800";

            VBox expireVBox = new VBox(expiresText);
            expireVBox.setAlignment(Pos.CENTER);
            expireVBox.setPadding(new Insets(3, 8, 3, 8));
            expireVBox.setStyle(
                    "-fx-background-color: " + badgeColor + ";" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-radius: 15;");

            expireVBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            StackPane.setAlignment(expireVBox, Pos.TOP_RIGHT);
            StackPane.setMargin(expireVBox, new Insets(6));
            imageContainer.getChildren().add(expireVBox);
        }

        // TITLE
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        titleText.setFill(Color.web("#2E3A59"));
        titleText.setWrappingWidth(190);

        // BRAND NAME
        Text brandText = new Text(brandName);
        brandText.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        brandText.setFill(Color.web("#666A7A"));
        HBox brandHBox = new HBox(brandText);
        brandHBox.setAlignment(Pos.CENTER_LEFT);

        // COUPON ID
        Text idText = new Text("ID: " + couponId);
        idText.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        idText.setFill(Color.web("#999999"));
        VBox idtextVbox = new VBox(idText);
        idtextVbox.setAlignment(Pos.CENTER_RIGHT);

        // STATUS BADGE
        Text statusText = new Text(status.toUpperCase());
        statusText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusText.setFill(Color.WHITE);

        String statusColor;
        switch (status.toLowerCase()) {
            case "active":
                statusColor = "#4CAF50";
                break; // Green
            case "pending":
                statusColor = "#FF9800";
                break; // Orange
            case "rejected":
                statusColor = "#E53935";
                break; // Red
            case "sold":
                statusColor = "#3F51B5";
                break; // Blue
            default:
                statusColor = "#9E9E9E"; // Grey
        }

        VBox statusBadge = new VBox(statusText);
        statusBadge.setAlignment(Pos.CENTER);
        statusBadge.setPadding(new Insets(3, 10, 3, 10));
        statusBadge.setStyle(
                "-fx-background-color: " + statusColor + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;");
        statusBadge.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Add all elements
        couponBody.getChildren().addAll(imageContainer, titleText, brandHBox, idtextVbox, statusBadge);

        // Hover Effect
        couponBody.setCursor(Cursor.HAND);
        couponBody.setOnMouseEntered(e -> couponBody.setStyle(
                "-fx-background-color: #FAFAFA;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #BDBDBD;" +
                        "-fx-border-width: 1.2;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 8);"));
        couponBody.setOnMouseExited(e -> couponBody.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-width: 1.2;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 0, 6);"));

        return couponBody;
    }

    // common coupon card for landing page
    public static VBox commonCouponCard(CouponDetails coupon) {
        String expiresIn = calculateExpiryText(coupon.getExpiry());

        VBox couponBody = createBaseCouponBody();
        couponBody.setSpacing(10);
        couponBody.setPadding(new Insets(12));
        // couponBody.setAlignment(Pos.TOP_CENTER);
        couponBody.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-width: 1.2;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 0, 6);");

        // IMAGE
        StackPane imageContainer = buildImageContainer(coupon.getCouponPosterURL(), expiresIn);
        imageContainer.setStyle("-fx-background-radius: 14;");

        // TITLE
        Text titleText = new Text(coupon.getCouponTitle());
        titleText.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        titleText.setFill(Color.web("#2E3A59"));
        titleText.setWrappingWidth(190);

        // BRAND NAME
        Text brandText = new Text(coupon.getBrandName());
        brandText.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        brandText.setFill(Color.web("#666A7A"));
        HBox brandHBox = new HBox(brandText);
        brandHBox.setAlignment(Pos.CENTER_LEFT);

        // PRICE
        Text priceField = new Text("Price : " + coupon.getCoinValue() + " Coins");
        priceField.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        priceField.setFill(Color.web("#1B4B91"));
        HBox priceHBox = new HBox(priceField);
        priceHBox.setAlignment(Pos.CENTER_LEFT);

        // COUPON ID
        Text idText = new Text("ID: " + coupon.getCouponId());
        idText.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        idText.setFill(Color.web("#999999"));

        VBox idtextVbox = new VBox(idText);
        idtextVbox.setAlignment(Pos.BOTTOM_RIGHT);
        idtextVbox.setPrefHeight(45);

        // Add all components
        couponBody.getChildren().addAll(imageContainer, titleText, brandHBox, priceHBox, idtextVbox);

        // Hover effect
        addHoverEffect(couponBody);

        // Navigation to description
        couponBody.setCursor(Cursor.HAND);
        couponBody.setOnMouseClicked(e -> PageManager.getInstance().showCouponDescription(coupon));

        return couponBody;
    }

    // Helper Method to Calculate Expiry Text
    private static String calculateExpiryText(String expiryDate) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate expiry = LocalDate.parse(expiryDate);

            long daysLeft = ChronoUnit.DAYS.between(today, expiry);

            if (daysLeft >= 15)
                return ""; // More than 15 days → show nothing
            if (daysLeft < 0)
                return "Expired";

            return daysLeft + "d left";
        } catch (Exception e) {
            return "";
        }
    }

    private static VBox createBaseCouponBody() {
        VBox couponBody = new VBox(10);
        couponBody.setAlignment(Pos.TOP_CENTER);
        couponBody.setPadding(new Insets(12));
        couponBody.setPrefWidth(220);
        couponBody.setPrefHeight(270);
        couponBody.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-width: 1.2;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 0, 6);");

        DropShadow shadow = new DropShadow();
        shadow.setRadius(6);
        shadow.setOffsetY(2);
        shadow.setColor(Color.rgb(0, 0, 0, 0.12));
        couponBody.setEffect(shadow);
        return couponBody;
    }

    private static StackPane buildImageContainer(String imageUrl, String expiresIn) {
        StackPane imageContainer = new StackPane();
        imageContainer.setAlignment(Pos.CENTER);

        // Optimized image loading with fixed size
        Image img = new Image(imageUrl, 200, 130, false, true);

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(200);
        imageView.setFitHeight(130);
        imageView.setSmooth(true);
        imageView.setPreserveRatio(false);

        // Rounded image corners
        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(18);
        clip.setArcHeight(18);
        imageView.setClip(clip);

        imageContainer.getChildren().add(imageView);

        // Expiry badge (if applicable)
        if (!expiresIn.isEmpty()) {
            Text expiresText = new Text(expiresIn);
            expiresText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
            expiresText.setFill(Color.WHITE);

            String badgeColor = expiresIn.equals("Expired") ? "#E53935" : "#FF9800";

            VBox expireVBox = new VBox(expiresText);
            expireVBox.setAlignment(Pos.CENTER);
            expireVBox.setPadding(new Insets(3, 8, 3, 8));
            expireVBox.setStyle(
                    "-fx-background-color: " + badgeColor + ";" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-radius: 15;");
            expireVBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            StackPane.setAlignment(expireVBox, Pos.TOP_RIGHT);
            StackPane.setMargin(expireVBox, new Insets(6));

            imageContainer.getChildren().add(expireVBox);
        }

        return imageContainer;
    }

    private static void addHoverEffect(VBox couponBody) {
        couponBody.setOnMouseEntered(e -> couponBody.setStyle(
                "-fx-background-color: #F8F8F8;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #BDBDBD;" +
                        "-fx-border-width: 1.2;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 14, 0, 0, 8);"));

        couponBody.setOnMouseExited(e -> couponBody.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-width: 1.2;" +
                        "-fx-border-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 0, 6);"));
    }
}
