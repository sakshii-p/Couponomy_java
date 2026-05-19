package com.superx.View;

import com.superx.Controller.PageManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LandingPage {

    Stage landingPageStage;
    Scene landingpageScene;
    public static BorderPane rootLayout;
    public static ScrollPane mainScrollPane;

    private final String[] carouselImages = {
            "/assets/AdBanner/baner1.png",
            "/assets/AdBanner/baner2.png",
            "/assets/AdBanner/baner3.png",
            "/assets/AdBanner/baner4.png"
    };

    public void setLandingPageStage(Stage landingPageStage) {
        this.landingPageStage = landingPageStage;
    }

    public void setLandingPageScene(Scene landingpageScene) {
        this.landingpageScene = landingpageScene;
    }

    public BorderPane createLandingPage() {
        rootLayout = new BorderPane();

        // 🔹 Header
        HBox header = StaticHeader.headerSection();

        // 🔹 Carousel
        StackPane carousel = createImageCarousel();
        carousel.setAlignment(Pos.TOP_CENTER);

        // ImageView carousel = new ImageView(new
        // Image("/assets/AdBanner/banner1.jpg"));
        // carousel.setFitHeight(400);
        // carousel.setFitWidth(1500); // adjust based on screen size
        // carousel.setPreserveRatio(false);
        // carousel.setSmooth(true);

        VBox categorySection = createCategoryGrid();
        VBox topBrandSection = createTopBrandsGrid();

        VBox mainVBox = new VBox(carousel, categorySection, topBrandSection);
        mainVBox.setSpacing(0);
        mainVBox.setPadding(new Insets(0));

        mainScrollPane = new ScrollPane(mainVBox);
        mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainScrollPane.setFitToWidth(true);

        rootLayout.setTop(header);
        rootLayout.setCenter(mainScrollPane);

        mainScrollPane.setStyle("-fx-background-color: transparent;");

        return rootLayout;
    }

    // For carouselPane

    private StackPane createImageCarousel() {
        StackPane carouselPane = new StackPane();
        carouselPane.setPrefHeight(400);
        carouselPane.setMaxWidth(Double.MAX_VALUE);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(400);
        imageView.setFitWidth(1550);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        carouselPane.getChildren().add(imageView);

        // Timeline animation
        Timeline timeline = new Timeline();
        final int[] index = { 0 };

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(3), event -> {
            Image img = new Image(carouselImages[index[0]]);
            imageView.setImage(img);
            index[0] = (index[0] + 1) % carouselImages.length;
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Start with the first image
        imageView.setImage(new Image(carouselImages[0]));

        return carouselPane;
    }

    private VBox createCategoryGrid() {
        VBox mainContainer = new VBox();
        mainContainer.setPadding(new Insets(40, 20, 40, 20));
        mainContainer.setSpacing(30);

        Label sectionTitle = new Label("Categories");
        sectionTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-font-family: 'Segoe UI';");

        HBox sectionTitleBox = new HBox(sectionTitle);
        sectionTitleBox.setAlignment(Pos.CENTER);

        String[][] categories = {
                { "Food & Beverages", "/assets/categories/Food.png" },
                { "Grocery", "/assets/categories/Grocery.png" },
                { "Fashion", "/assets/categories/Fashion.png" },
                { "Electronics", "/assets/categories/Electronics.png" },
                { "Beauty & Personal Care", "/assets/categories/Beauty.png" },
                { "Medicines & Health", "/assets/categories/Medicines.png" },
                { "Travel", "/assets/categories/Travel.png" },
                { "Entertainment", "/assets/categories/Entertainment.png" },
                { "Online Shopping", "/assets/categories/OnlineShopping.png" },
                { "Home & Kitchen", "/assets/categories/Home.png" },
                { "Recharge & Bills", "/assets/categories/Recharge.png" },
                { "Subscription Services", "/assets/categories/Subscription.png" },
                { "See All", "assets/categories/sellall.jpg" }
        };

        // Row layout plan
        int[] rowCounts = { 4, 5, 4 };

        VBox rowsContainer = new VBox(20);

        int index = 0;
        for (int rowSize : rowCounts) {
            HBox row = new HBox(30);
            row.setAlignment(Pos.CENTER);

            for (int i = 0; i < rowSize && index < categories.length; i++, index++) {
                String name = categories[index][0];
                String imgPath = categories[index][1];

                VBox button = new VBox(12);
                button.setEffect(new GaussianBlur(12));
                button.setAlignment(Pos.CENTER);
                button.setPadding(new Insets(15));
                button.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.25);" +
                                "-fx-background-insets: 0;" +
                                "-fx-border-radius: 14;" +
                                "-fx-background-radius: 14;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0.2, 0, 4);" +
                                "-fx-background-radius: 14;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);" +
                                "-fx-cursor: hand;");

                ImageView image = new ImageView(new Image(imgPath));
                image.setFitWidth(175);
                image.setFitHeight(150);
                image.setPreserveRatio(true);

                Label label = new Label(name);
                label.setStyle("-fx-font-size: 16px; -fx-font-family: 'Segoe UI'; -fx-text-alignment: center;");
                label.setWrapText(true);
                label.setMaxWidth(140);

                HBox labelHBox = new HBox(label);
                labelHBox.setAlignment(Pos.CENTER);

                button.getChildren().addAll(image, labelHBox);

                // 🔹 Add navigation logic
                button.setOnMouseClicked(event -> {
                    if (name.equalsIgnoreCase("See All")) {
                        PageManager.getInstance().showCouponListPage(null); // null = show all coupons
                    } else {
                        PageManager.getInstance().showCouponListPage(name);
                    }
                });

                row.getChildren().add(button);
            }

            rowsContainer.getChildren().add(row);
        }

        mainContainer.getChildren().addAll(sectionTitleBox, rowsContainer);
        mainContainer
                .setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #5b3db5, #b097eb);");

        return mainContainer;
    }

    private VBox createTopBrandsGrid() {
        VBox mainContainer = new VBox();
        mainContainer.setPadding(new Insets(40, 20, 40, 20));
        mainContainer.setSpacing(30);

        Label sectionTitle = new Label("Top Brands");
        sectionTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-font-family: 'Segoe UI';");

        HBox sectionTitleBox = new HBox(sectionTitle);
        sectionTitleBox.setAlignment(Pos.CENTER);

        String[][] brands = {
                { "Amazon", "/assets/brands/amazon.png" },
                { "Swiggy", "/assets/brands/swiggy.png" },
                { "Zomato", "/assets/brands/zomato.png" },
                { "Domino's", "/assets/brands/dominos.png" },
                { "Ola", "/assets/brands/ola.png" },
                { "Blinkit", "/assets/brands/blinkit.png" },
                { "Nykaa", "/assets/brands/nykaa.png" },
        };

        int[] rowCounts = { 3, 4 };

        VBox rowsContainer = new VBox(20);
        int index = 0;

        for (int rowSize : rowCounts) {
            HBox row = new HBox(30);
            row.setAlignment(Pos.CENTER);

            for (int i = 0; i < rowSize && index < brands.length; i++, index++) {
                String name = brands[index][0];
                String imgPath = brands[index][1];

                VBox button = new VBox(10);
                button.setEffect(new GaussianBlur(12));
                button.setAlignment(Pos.CENTER);
                button.setPadding(new Insets(15));
                button.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.25);" +
                                "-fx-background-insets: 0;" +
                                "-fx-border-radius: 14;" +
                                "-fx-background-radius: 14;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0.2, 0, 4);" +
                                "-fx-background-radius: 14;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);" +
                                "-fx-cursor: hand;");

                ImageView image = new ImageView(new Image(imgPath));
                image.setFitWidth(175); // Slightly smaller than category
                image.setFitHeight(150);
                image.setPreserveRatio(false);

                Label label = new Label(name);
                label.setStyle("-fx-font-size: 16px; -fx-font-family: 'Segoe UI'; -fx-text-alignment: center;");
                label.setWrapText(true);
                label.setMaxWidth(120);

                HBox labelHBox = new HBox(label);
                labelHBox.setAlignment(Pos.CENTER);

                button.getChildren().addAll(image, labelHBox);

                // 🔹 Add navigation logic
                button.setOnMouseClicked(event -> {
                    PageManager.getInstance().showCouponListPage(name); // pass category name
                });

                row.getChildren().add(button);

            }

            rowsContainer.getChildren().add(row);

        }

        VBox footerContainer = new VBox();
        footerContainer.setAlignment(Pos.CENTER);
        footerContainer.setSpacing(10);
        footerContainer.setPadding(new Insets(15));
        footerContainer
                .setStyle("-fx-background-color: transparent; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");

        // Footer Text
        Text footerText = new Text("© 2025 Couponomy | All rights reserved");
        footerText.setFont(Font.font("Segoe UI", 13));
        footerText.setFill(javafx.scene.paint.Color.web("#555555"));

        // About Us Link
        Text aboutUsLink = new Text("About Us");
        aboutUsLink.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        aboutUsLink.setFill(javafx.scene.paint.Color.web("#4B0082"));
        aboutUsLink.setUnderline(true);
        aboutUsLink.setCursor(Cursor.HAND);

        // Hover Effect
        aboutUsLink.setOnMouseEntered(e -> aboutUsLink.setFill(javafx.scene.paint.Color.web("#6A0DAD")));
        aboutUsLink.setOnMouseExited(e -> aboutUsLink.setFill(javafx.scene.paint.Color.web("#4B0082")));

        // Click Action (redirect to About Us page)
        aboutUsLink.setOnMouseClicked(e -> {
            PageManager.getInstance().showAboutUsPage();
            System.out.println("About Us Clicked!");
        });

        // Combine
        HBox links = new HBox(10, aboutUsLink);
        links.setAlignment(Pos.CENTER);

        footerContainer.getChildren().addAll(footerText, links);

        mainContainer.getChildren().addAll(sectionTitleBox, rowsContainer, footerContainer);
        mainContainer
                .setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #5b3db5, #b097eb);");

        return mainContainer;
    }
}