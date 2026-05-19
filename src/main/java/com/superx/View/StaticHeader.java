package com.superx.View;

import org.kordamp.ikonli.javafx.FontIcon;

import com.superx.Controller.PageManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class StaticHeader {
    public static HBox headerSection() {

        HBox header = new HBox();
        header.setStyle(
                "-fx-background-color: linear-gradient(to right, #5035b8, #5035b8);" +
                        "-fx-border-color: #5035b8;" +
                        "-fx-border-width: 0 0 1px 0;");

        header.setPadding(new Insets(10, 20, 10, 20));
        header.setPrefHeight(70);
        header.setAlignment(Pos.CENTER);
        header.setSpacing(30);

        // Left: Logo
        ImageView logo = new ImageView(new Image("assets/images/image.png"));
        logo.setFitHeight(40);
        logo.setPreserveRatio(true);
        logo.setOnMouseClicked(e -> {
            LandingPage.rootLayout.setCenter(PageManager.getInstance().showMainLandingPage());
        });

        HBox leftBox = new HBox(logo);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        // Center: Search and Categories
        TextField searchField = new TextField();
        searchField.setPromptText("Search for coupons...");
        searchField.setPrefWidth(400);
        searchField.setPrefHeight(36);
        searchField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 1);" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: rgba(0, 0, 0, 1);" +
                        "-fx-border-radius: 12;" +
                        "-fx-font-size: 14px;" +
                        "-fx-prompt-text-fill: #000000ff;" +
                        "-fx-text-fill: black;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.42), 4, 0.3, 0, 2);" +
                        "-fx-padding: 0 15 0 15;");
        searchField.setOnAction(e -> {
            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) {
                PageManager.getInstance().showCouponListPageWithSearch(searchText);
            } else {
                PageManager.getInstance().showCouponListPage(); // Show all if empty
            }
        });

        MenuButton categories = new MenuButton("Categories");
        categories.setPrefHeight(36);
        categories.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-border-radius: 12;" +
                        "-fx-text-fill: white;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.2), 4, 0.3, 0, 2);" +
                        "-fx-padding: 0 15 0 15;");
        String[] categoryNames = {
                "Food & Beverages", "Grocery", "Fashion", "Electronics",
                "Beauty & Personal Care", "Medicines & Health", "Travel",
                "Entertainment", "Online Shopping", "Home & Kitchen",
                "Recharge & Bills", "Subscription Services"
        };

        for (String cat : categoryNames) {
            MenuItem item = new MenuItem(cat);
            item.setOnAction(e -> {
                PageManager.getInstance().showCouponListPage(cat);
            });
            categories.getItems().add(item);
        }

        HBox centerBox = new HBox(15, searchField, categories);
        centerBox.setAlignment(Pos.CENTER);

        // Right: Sell, Cart, User
        Button sellCoupon = new Button("Sell a Coupon");
        sellCoupon.setStyle(
                "-fx-background-color: #b096ec;" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 8 20 8 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 8, 0.3, 0, 4);" +
                        "-fx-cursor: hand;");

        sellCoupon.setPrefHeight(36);
        sellCoupon.setOnAction(e -> {
            PageManager.getInstance().showSellingPage();
        });

        FontIcon cartIcon = new FontIcon("fas-cart-plus");
        cartIcon.setIconSize(18);
        Button cartBtn = new Button("", cartIcon);
        cartBtn.setStyle("-fx-background-color: transparent;");
        cartBtn.setOnAction(e -> {
            PageManager.getInstance().showCartSection();
        });

        FontIcon profileIcon = new FontIcon("fas-user");
        profileIcon.setIconSize(18);
        Button userBtn = new Button("", profileIcon);
        userBtn.setStyle("-fx-background-color: transparent;");
        userBtn.setOnAction(e -> {
            PageManager.getInstance().showProfileSection();
        });

        HBox rightBox = new HBox(15, sellCoupon, cartBtn, userBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        // Spacer regions to center content
        Region spacerLeft = new Region();
        Region spacerRight = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        header.getChildren().addAll(leftBox, spacerLeft, centerBox, spacerRight, rightBox);
        return header;
    }
}