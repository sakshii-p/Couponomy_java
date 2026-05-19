package com.superx.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class profilePage {

        VBox rightContainer;

        public HBox createProfileHBox() {

                HBox root = new HBox();
                root.setPadding(new Insets(0));
                root.setSpacing(0);

                rightContainer = new VBox(20);
                rightContainer.setAlignment(Pos.TOP_CENTER);
                rightContainer.setPadding(new Insets(30));
                rightContainer.setPrefWidth(900);
                rightContainer.setStyle("-fx-background-color: #E6E6FA;");

                rightContainer.getChildren().add(new ProfileSection().getPage());

                VBox leftContainer = new VBox(15);
                leftContainer.setPadding(new Insets(25));
                leftContainer.setAlignment(Pos.TOP_CENTER);
                leftContainer.setPrefWidth(600);
                leftContainer.setStyle("-fx-background-color: #8d42ffff;");

                // Button Styles
                String defaultStyle = "-fx-background-color: #ffffff; -fx-background-radius: 10;"
                                + "-fx-border-radius: 10; -fx-border-color: #ddd; -fx-cursor: hand;";
                String hoverStyle = "-fx-background-color: #f0e6ff; -fx-background-radius: 10;"
                                + "-fx-border-radius: 10; -fx-border-color: #c9aaff; -fx-cursor: hand;";

                // Add all menu buttons
                leftContainer.getChildren().addAll(
                                createCard("Profile", "View and edit your profile details",
                                                "https://cdn-icons-png.flaticon.com/512/3135/3135715.png", defaultStyle,
                                                hoverStyle),
                                createCard("Transaction History", "Check your past payments and transactions",
                                                "https://cdn-icons-png.flaticon.com/512/2331/2331946.png", defaultStyle,
                                                hoverStyle),
                                createCard("Wallet", "View your Couponmy wallet balance",
                                                "https://cdn-icons-png.flaticon.com/512/1170/1170576.png", defaultStyle,
                                                hoverStyle),
                                createCard("My Coupons", "View all the coupons you purchased and sold",
                                                "https://cdn-icons-png.flaticon.com/512/3063/3063824.png", defaultStyle,
                                                hoverStyle),
                                createCard("Help & Support", "Contact our customer service",
                                                "https://cdn-icons-png.flaticon.com/512/1827/1827504.png", defaultStyle,
                                                hoverStyle),
                                createCard("Report Issue", "Report coupon-related problems",
                                                "https://cdn-icons-png.flaticon.com/512/3601/3601740.png", defaultStyle,
                                                hoverStyle),
                                createCard("FAQs", "Frequently asked questions",
                                                "https://cdn-icons-png.flaticon.com/512/545/545676.png", defaultStyle,
                                                hoverStyle));

                root.getChildren().addAll(leftContainer, rightContainer);
                return root;
        }

        private Button createCard(String title, String description, String iconUrl, String defaultStyle,
                        String hoverStyle) {
                ImageView icon = new ImageView(new Image(iconUrl, 45, 45, true, true));

                Text titleText = new Text(title);
                titleText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
                titleText.setFill(Color.web("#212121"));

                Text descText = new Text(description);
                descText.setFont(Font.font("Segoe UI", 14));
                descText.setFill(Color.web("#555"));
                descText.setWrappingWidth(300);

                VBox textBox = new VBox(5, titleText, descText);
                textBox.setAlignment(Pos.CENTER_LEFT);

                HBox content = new HBox(15, icon, textBox);
                content.setAlignment(Pos.CENTER_LEFT);

                Button card = new Button();
                card.setGraphic(content);
                card.setPrefWidth(500);
                card.setPrefHeight(90);
                card.setStyle(defaultStyle);

                DropShadow shadow = new DropShadow();
                shadow.setRadius(5);
                shadow.setOffsetX(1.5);
                shadow.setOffsetY(1.5);
                shadow.setColor(Color.rgb(0, 0, 0, 0.15));
                card.setEffect(shadow);

                // Hover Animation
                card.setOnMouseEntered(e -> {
                        card.setStyle(hoverStyle);
                        card.setScaleX(1.02);
                        card.setScaleY(1.02);
                });
                card.setOnMouseExited(e -> {
                        card.setStyle(defaultStyle);
                        card.setScaleX(1);
                        card.setScaleY(1);
                });

                // Switching Pages (Right Panel)
                card.setOnAction(e -> {
                        rightContainer.getChildren().clear();
                        switch (title) {
                                case "Profile" -> rightContainer.getChildren().add(new ProfileSection().getPage());
                                case "Transaction History" ->
                                        rightContainer.getChildren().add(new TransactionHistorySection().getPage());
                                case "Wallet" -> rightContainer.getChildren().add(new WalletSection().getPage());
                                case "My Coupons" -> rightContainer.getChildren().add(new MyCouponsSection().getPage());
                                case "Help & Support" ->
                                        rightContainer.getChildren().add(new HelpSupportSection().getPage());
                                case "Report Issue" ->
                                        rightContainer.getChildren().add(new ReportIssueSection().getPage());
                                case "FAQs" -> rightContainer.getChildren().add(new FAQsSection().getPage());
                        }
                });

                return card;
        }

}
