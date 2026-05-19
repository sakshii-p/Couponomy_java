package com.superx.View;

import java.util.List;

import com.superx.Controller.CouponManager;
import com.superx.Model.CouponDetails;
import com.superx.Model.UserSession;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MyCouponsSection {

    public VBox getPage() {
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #F8F8FF, #FFFFFF);");
        root.setPadding(new Insets(20));

        // ✅ Title
        Text title = new Text("My Coupons");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setFill(Color.web("#4B0082"));

        // ✅ TabPane (Headers Hidden)
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: transparent;");
        tabPane.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                Region region = (Region) newSkin.getNode();
                region.lookup(".tab-header-area").setVisible(false);
                region.lookup(".tab-header-area").setManaged(false);
            }
        });

        Tab purchasedTab = new Tab("Purchased", createPurchasedSection());
        Tab soldTab = new Tab("Listed", createSoldSection());
        tabPane.getTabs().addAll(purchasedTab, soldTab);

        // Custom Toggle Buttons to Switch Tabs
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton purchasedBtn = new ToggleButton("Purchased");
        ToggleButton soldBtn = new ToggleButton("Listed");
        purchasedBtn.setToggleGroup(toggleGroup);
        soldBtn.setToggleGroup(toggleGroup);
        purchasedBtn.setSelected(true);

        // Styling Buttons like Tabs
        String normalStyle = "-fx-background-color: #E6E6FA; -fx-text-fill: #4B0082; -fx-font-weight: bold; -fx-background-radius: 8;";
        String selectedStyle = "-fx-background-color: #4B0082; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;";
        purchasedBtn.setStyle(selectedStyle);
        soldBtn.setStyle(normalStyle);

        purchasedBtn.setOnAction(e -> {
            tabPane.getSelectionModel().select(purchasedTab);
            purchasedBtn.setStyle(selectedStyle);
            soldBtn.setStyle(normalStyle);
        });
        soldBtn.setOnAction(e -> {
            tabPane.getSelectionModel().select(soldTab);
            soldBtn.setStyle(selectedStyle);
            purchasedBtn.setStyle(normalStyle);
        });

        // Button Layout
        HBox tabButtons = new HBox(10, purchasedBtn, soldBtn);
        tabButtons.setAlignment(Pos.CENTER_LEFT);
        tabButtons.setPadding(new Insets(10, 0, 10, 0));

        root.getChildren().addAll(title, tabButtons, tabPane);
        return root;
    }

    // Purchased Coupons Section (Grid Layout)
    private ScrollPane createPurchasedSection() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(20);
        grid.setVgap(20);

        String currentUserId = UserSession.getCurrentUser().getUserId();
        List<CouponDetails> purchasedCoupons = CouponManager.getPurchasedCoupons(currentUserId);

        int column = 0, row = 0;
        for (CouponDetails coupon : purchasedCoupons) {
            VBox card = CouponCard.buyCouponCard(coupon);
            grid.add(card, column, row);
            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scrollPane;
    }

    private ScrollPane createSoldSection() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(20);
        grid.setVgap(20);

        String currentUserId = UserSession.getCurrentUser().getUserId();
        List<CouponDetails> soldCoupons = CouponManager.getListedCoupons(currentUserId);

        int column = 0, row = 0;
        for (CouponDetails coupon : soldCoupons) {
            VBox card = CouponCard.sellCouponCard(
                    coupon.getCouponPosterURL(),
                    coupon.getExpiry(),
                    coupon.getCouponTitle(),
                    coupon.getBrandName(),
                    coupon.getCouponId(),
                    coupon.getStatus());
            grid.add(card, column, row);
            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scrollPane;
    }
}