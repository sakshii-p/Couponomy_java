package com.superx.View;

import com.superx.Controller.CartManager;
import com.superx.Controller.PageManager;
import com.superx.Model.CouponDetails;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CouponDescription {

        public VBox createCouponDescription(CouponDetails coupon) {
                boolean isPurchased = coupon.getBuyerId() != null && !coupon.getBuyerId().trim().isEmpty();

                // Root container
                VBox root = new VBox();
                root.setPadding(new Insets(30));
                root.setAlignment(Pos.CENTER);
                root.setStyle("-fx-background-color: linear-gradient(to bottom, #f6f9fc, #eef1f5);");

                // ===== COUPON CARD =====
                HBox card = new HBox(40);
                card.setPadding(new Insets(35));
                card.setStyle(
                                "-fx-background-color: white;" +
                                                "-fx-background-radius: 18;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 8);" +
                                                "-fx-border-color: #e0e0e0; -fx-border-radius: 18;");
                card.setMaxWidth(920);
                card.setMinWidth(920);
                card.setAlignment(Pos.CENTER_LEFT);

                HBox cardWrapper = new HBox(card);
                cardWrapper.setAlignment(Pos.CENTER);
                cardWrapper.setPadding(new Insets(60, 0, 60, 0));

                // ===== IMAGE =====
                ImageView couponImage = new ImageView(
                                loadImageSafe(
                                                coupon.getCouponPosterURL(),
                                                "assets/images/couponbackground.png",
                                                360, 260));
                couponImage.setStyle(
                                "-fx-background-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4);");

                // ===== INFO SECTION =====
                VBox infoBox = new VBox(12);
                infoBox.setAlignment(Pos.TOP_LEFT);

                String safeTitle = defaultIfBlank(coupon.getCouponTitle(), coupon.getBrandName() + " Coupon");
                Label titleLabel = new Label(safeTitle);
                titleLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 32));
                titleLabel.setTextFill(Color.web("#2E3A59"));

                Label metaLabel = new Label(
                                "Brand: " + defaultIfBlank(coupon.getBrandName(), "-") +
                                                "   |   Category: " + defaultIfBlank(coupon.getCategory(), "-") +
                                                "   |   Type: " + defaultIfBlank(coupon.getCouponType(), "-"));
                metaLabel.setFont(Font.font("Segoe UI", 16));
                metaLabel.setTextFill(Color.web("#666A7A"));

                // Price ABOVE Offer
                Label coinLabel = new Label("Price: " + coupon.getCoinValue() + " Coins");
                coinLabel.setFont(Font.font("Tahoma", FontWeight.EXTRA_BOLD, 24));
                coinLabel.setTextFill(Color.web("#000000ff"));

                Label offerLabel = new Label("Offer: " + defaultIfBlank(coupon.getOfferValue(), "-"));
                offerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16)); // Reduced size
                offerLabel.setTextFill(Color.web("#27AE60"));

                Label minOrderLabel = new Label("Min Order Value: ₹" + defaultIfBlank(coupon.getMinOrdValue(), "-"));
                minOrderLabel.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 16)); // Reduced size
                minOrderLabel.setTextFill(Color.web("#2C3E50"));

                Label expiryLabel = new Label("Expiry: " + defaultIfBlank(coupon.getExpiry(), "-"));
                expiryLabel.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 16)); // Reduced size
                expiryLabel.setTextFill(Color.web("#C0392B"));

                VBox offexpminVBox = new VBox(6, offerLabel, minOrderLabel, expiryLabel);
                Label codeLabel = null;
                if (isPurchased && coupon.getCouponCode() != null && !coupon.getCouponCode().isEmpty()) {
                        codeLabel = new Label("Coupon Code: " + coupon.getCouponCode());
                        codeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                        codeLabel.setTextFill(Color.web("#16A085"));
                }

                Label decLabel = new Label("Description : ");
                decLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
                Label descLabel = new Label(defaultIfBlank(coupon.getDescription(), "-"));
                descLabel.setFont(Font.font("Segoe UI", 14));
                descLabel.setWrapText(true);
                descLabel.setTextFill(Color.web("#4A4A4A"));

                // ===== BUTTONS =====
                HBox btnBox = new HBox(15);
                btnBox.setAlignment(Pos.CENTER_LEFT);

                Button backBtn = new Button("← Back");
                backBtn.setStyle(
                                "-fx-background-color: #ECECEC; -fx-text-fill: #2E3A59; " +
                                                "-fx-padding: 8 16; -fx-background-radius: 8; -fx-font-size: 14px;" +
                                                "-fx-cursor: hand;");
                backBtn.setOnAction(e -> PageManager.getInstance().goBack());

                Button addToCartBtn = new Button("🛒 Add to Cart");
                addToCartBtn.setStyle(
                                "-fx-background-color: #28a745; -fx-text-fill: white; " +
                                                "-fx-font-weight: bold; -fx-padding: 10 18; -fx-background-radius: 8; "
                                                +
                                                "-fx-font-size: 15px; -fx-cursor: hand;");
                addToCartBtn.setOnAction(e -> {
                        CartManager.getInstance().add(coupon);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Cart");
                        alert.setHeaderText(null);
                        alert.setContentText("Coupon added to cart!");
                        alert.showAndWait();
                        PageManager.getInstance().goBack();
                });

                btnBox.getChildren().addAll(backBtn, addToCartBtn);

                if (codeLabel != null) {
                        infoBox.getChildren().addAll(titleLabel, metaLabel, coinLabel, offexpminVBox, codeLabel,
                                        decLabel, descLabel, btnBox);
                } else {
                        infoBox.getChildren().addAll(titleLabel, metaLabel, coinLabel, offexpminVBox, decLabel,
                                        descLabel, btnBox);
                }

                card.getChildren().addAll(couponImage, infoBox);
                root.getChildren().add(cardWrapper);

                return root;
        }

        private Image loadImageSafe(String url, String fallbackPath, double w, double h) {
                try {
                        if (url != null && !url.isBlank()) {
                                return new Image(url, w, h, true, true);
                        }
                } catch (Exception ignored) {
                }
                // Fallback to a local placeholder bundled with your app
                return new Image(fallbackPath, w, h, true, true);
        }

        private String defaultIfBlank(String s, String def) {
                return (s == null || s.trim().isEmpty()) ? def : s;
        }

}
