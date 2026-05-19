package com.superx.View;

import com.superx.Controller.CartManager;
import com.superx.Controller.PageManager;
import com.superx.Controller.WalletManager;
import com.superx.Model.CouponDetails;
import com.superx.Model.UserSession;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CartSection {
    private ObservableList<CouponDetails> cartCoupons = CartManager.getInstance().getItems();
    private Label coinBalanceLabel;

    public StackPane createCart() {
        // Full screen root
        StackPane fullscreenRoot = new StackPane();
        fullscreenRoot.setStyle("-fx-background-color: linear-gradient(to bottom right, #EFEFEF, #DFF6FF);");

        // Centered cart box
        VBox cartBox = new VBox(20);
        cartBox.setAlignment(Pos.CENTER);
        cartBox.setPadding(new Insets(40));
        cartBox.setMaxWidth(480);
        cartBox.setStyle(
                "-fx-background-color: white; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, #AAA, 12, 0, 0, 3);");

        // Title
        Label cartTitle = new Label("🛒 Your Cart");
        cartTitle.setFont(Font.font("Arial Black", 30));
        cartTitle.setPadding(new Insets(0, 0, 10, 0));

        // Cart coupon display area (scrollable)
        VBox couponsListBox = new VBox(16);
        couponsListBox.setPrefWidth(400);

        ScrollPane scrollPane = new ScrollPane(couponsListBox);
        scrollPane.setPrefHeight(320);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Coin balance and cart value
        coinBalanceLabel = new Label();
        coinBalanceLabel.setFont(Font.font("Segoe UI", 18));
        updateCoinBalance();

        // Proceed button with payment logic
        Button proceedBtn = new Button("Proceed to Payment");
        proceedBtn.setFont(Font.font("Segoe UI Semibold", 16));
        proceedBtn.setStyle("-fx-background-color: #25A8E0; -fx-text-fill: white; -fx-background-radius: 12;");
        proceedBtn.setMinHeight(40);
        proceedBtn.setMaxWidth(Double.MAX_VALUE);
        proceedBtn.setOnAction(e -> {
            int cartTotal = cartCoupons.stream().mapToInt(CouponDetails::getCoinValue).sum();

            if (cartTotal == 0) {
                new Alert(Alert.AlertType.INFORMATION, "Your cart is empty!").showAndWait();
                return;
            }

            int buyerBalance = Integer.parseInt(UserSession.getCurrentUser().getWalletCoins());
            if (cartTotal > buyerBalance) {
                new Alert(Alert.AlertType.ERROR, "Insufficient coins for this payment.").showAndWait();
                return;
            }

            try {
                // Deduct buyer coins
                int updatedBalance = buyerBalance - cartTotal;
                UserSession.getCurrentUser().setWalletCoins(Integer.toString(updatedBalance));
                WalletManager.updateUserWallet(UserSession.getCurrentUser().getUserId(), updatedBalance);

                // Process each coupon
                for (CouponDetails coupon : cartCoupons) {
                    WalletManager.addCoinsToSeller(coupon.getSellerId(), coupon.getCoinValue());
                    coupon.setStatus("sold");
                    coupon.setVisibility("private");
                    coupon.setBuyerId(UserSession.getCurrentUser().getUserId());
                    WalletManager.updateCouponAfterPurchase(coupon);
                }

                // Clear cart and refresh
                cartCoupons.clear();
                refreshCouponsList(couponsListBox);

                new Alert(Alert.AlertType.INFORMATION, "Payment Successful!").showAndWait();

            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Payment failed! Try again.").showAndWait();
            }
        });

        // Back Button
        Button backBtn = new Button("← Back");
        backBtn.setFont(Font.font("Segoe UI Semibold", 14));
        backBtn.setStyle("-fx-background-color: #CCC; -fx-text-fill: black; -fx-background-radius: 8;");
        backBtn.setOnAction(e -> PageManager.getInstance().showLandingPage());

        // Add everything to the cart box
        cartBox.getChildren().addAll(backBtn, cartTitle, scrollPane, coinBalanceLabel, proceedBtn);
        fullscreenRoot.getChildren().add(cartBox);

        // Initialize display
        refreshCouponsList(couponsListBox);

        return fullscreenRoot;
    }

    // Update coin balance based on items in the cart
    private void updateCoinBalance() {
        int total = cartCoupons.stream().mapToInt(CouponDetails::getCoinValue).sum();
        int balance = Integer.parseInt(UserSession.getCurrentUser().getWalletCoins());
        coinBalanceLabel.setText("💰 Coins Balance: " + balance + "   |   Cart Value: " + total);
    }

    // Refresh the visual list of coupons (and recalculate coin balance)
    private void refreshCouponsList(VBox couponsListBox) {
        couponsListBox.getChildren().clear();
        for (CouponDetails coupon : cartCoupons) {
            HBox card = createCouponCard(coupon, couponsListBox);
            couponsListBox.getChildren().add(card);
        }
        updateCoinBalance();
    }

    // Coupon card with title, value, expire, and Remove at bottom right
    private HBox createCouponCard(CouponDetails coupon, VBox couponsListBox) {
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label(coupon.getCouponTitle());
        titleLabel.setFont(Font.font("Segoe UI Semibold", 19));
        titleLabel.setTextFill(Color.web("#FFBF00")); // Gold

        Label valueLabel = new Label("Value: " + coupon.getCoinValue() + " coins");
        valueLabel.setFont(Font.font("Segoe UI", 14));
        valueLabel.setTextFill(Color.web("#26ffe6")); // Turquoise

        Label expireLabel = new Label("Expires: " + coupon.getExpiry());
        expireLabel.setFont(Font.font("Segoe UI", 14));
        expireLabel.setTextFill(Color.web("#FF6E6E")); // Soft red

        infoBox.getChildren().addAll(titleLabel, valueLabel, expireLabel);

        // RIGHT: Remove
        StackPane rightPane = new StackPane();
        rightPane.setPrefWidth(80);

        Label removeLabel = new Label("Remove");
        removeLabel.setFont(Font.font("Segoe UI", 11));
        removeLabel.setTextFill(Color.web("#ff6e6e"));
        removeLabel.setStyle("-fx-cursor: hand; -fx-underline: true;");
        removeLabel.setPadding(new Insets(8));
        removeLabel.setOnMouseClicked((MouseEvent e) -> {
            cartCoupons.remove(coupon);
            refreshCouponsList(couponsListBox);
        });

        StackPane.setAlignment(removeLabel, Pos.BOTTOM_RIGHT);
        rightPane.getChildren().add(removeLabel);

        // Root card HBox
        HBox cardRoot = new HBox(12, infoBox, rightPane);
        cardRoot.setPadding(new Insets(16, 18, 12, 18));
        cardRoot.setMaxWidth(Double.MAX_VALUE);
        cardRoot.setMinHeight(90);
        cardRoot.setAlignment(Pos.TOP_LEFT);
        cardRoot.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #232428 80%, #2A2B32 100%);" +
                        " -fx-background-radius: 11;" +
                        " -fx-effect: dropshadow(gaussian, #333c, 8,0,0,1);");
        return cardRoot;
    }
}
