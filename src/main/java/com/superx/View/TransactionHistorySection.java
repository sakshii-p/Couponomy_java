package com.superx.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class TransactionHistorySection {

    private VBox transactionList;

    public VBox getPage() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #F8F8FF;");
        root.setAlignment(Pos.TOP_CENTER);

        // Title
        Text title = new Text("Transaction History");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setFill(Color.web("#4B0082"));

        // Filter Buttons
        HBox filterButtons = createFilterButtons();

        // Transaction List (initially All)
        transactionList = createTransactionList("All");

        // Back Button
        Button backBtn = new Button("← Back to Wallet");
        backBtn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        backBtn.setTextFill(Color.WHITE);
        backBtn.setCursor(Cursor.HAND);
        backBtn.setStyle("-fx-background-color: #4B0082; -fx-background-radius: 8;");
        backBtn.setOnAction(e -> System.out.println("Returning to Wallet Page..."));

        root.getChildren().addAll(title, filterButtons, transactionList, backBtn);
        return root;
    }

    // Filter Buttons
    private HBox createFilterButtons() {
        ToggleGroup group = new ToggleGroup();
        ToggleButton allBtn = createFilterButton("All", group, true);
        ToggleButton creditsBtn = createFilterButton("Credits", group, false);
        ToggleButton debitsBtn = createFilterButton("Debits", group, false);

        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateTransactionList(((ToggleButton) newVal).getText());
            }
        });

        HBox box = new HBox(10, allBtn, creditsBtn, debitsBtn);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private ToggleButton createFilterButton(String text, ToggleGroup group, boolean selected) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setSelected(selected);
        btn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        btn.setCursor(Cursor.HAND);
        btn.setStyle("-fx-background-color: #EDE7F6; -fx-text-fill: #4B0082; -fx-background-radius: 6;");
        btn.setOnMouseEntered(
                e -> btn.setStyle("-fx-background-color: #D1C4E9; -fx-text-fill: #4B0082; -fx-background-radius: 6;"));
        btn.setOnMouseExited(e -> {
            if (!btn.isSelected())
                btn.setStyle("-fx-background-color: #EDE7F6; -fx-text-fill: #4B0082; -fx-background-radius: 6;");
        });
        btn.setOnAction(
                e -> btn.setStyle("-fx-background-color: #4B0082; -fx-text-fill: white; -fx-background-radius: 6;"));
        return btn;
    }

    private void updateTransactionList(String filter) {
        VBox parent = (VBox) transactionList.getParent();
        parent.getChildren().remove(transactionList);
        transactionList = createTransactionList(filter);
        parent.getChildren().add(2, transactionList); // add back in correct position
    }

    // Transaction List
    private VBox createTransactionList(String filter) {
        VBox list = new VBox(8);

        // Dummy Transactions (replace with DB later)
        String[][] transactions = {
                { "Purchased Amazon Coupon", "-200 Coins", "#d32f2f", "✅", "18 July 2025" },
                { "Sold Flipkart Coupon", "+300 Coins", "#2e7d32", "✅", "16 July 2025" },
                { "Purchased Zomato Coupon", "-150 Coins", "#d32f2f", "⏳", "15 July 2025" },
                { "Admin Bonus", "+100 Coins", "#2e7d32", "✅", "14 July 2025" },
                { "Withdraw Request", "-500 Coins", "#d32f2f", "❌", "13 July 2025" },
                { "Referral Bonus", "+200 Coins", "#2e7d32", "✅", "12 July 2025" }
        };

        for (String[] t : transactions) {
            boolean isCredit = t[1].startsWith("+");
            if (filter.equals("Credits") && !isCredit)
                continue;
            if (filter.equals("Debits") && isCredit)
                continue;
            list.getChildren().add(createTransactionItem(t[0], t[1], t[2], t[3], t[4]));
        }

        list.setPadding(new Insets(10));
        list.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        list.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.1)));

        // Scrollable if too many transactions
        ScrollPane scrollPane = new ScrollPane(list);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox container = new VBox(10, new Text("All Transactions"), scrollPane);
        return container;
    }

    private HBox createTransactionItem(String detail, String amount, String color, String status, String date) {
        Text detailText = new Text(detail);
        detailText.setFont(Font.font("Segoe UI", 14));
        detailText.setFill(Color.web("#555"));

        Text amountText = new Text(amount);
        amountText.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        amountText.setFill(Color.web(color));

        Text dateText = new Text(date);
        dateText.setFont(Font.font("Segoe UI", 12));
        dateText.setFill(Color.web("#777"));

        Text statusText = new Text(status);
        statusText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        if (status.equals("✅"))
            statusText.setFill(Color.web("#2e7d32"));
        else if (status.equals("⏳"))
            statusText.setFill(Color.web("#f57c00"));
        else
            statusText.setFill(Color.web("#d32f2f"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(10, detailText, spacer, amountText, statusText, dateText);
        row.setPadding(new Insets(8, 5, 8, 5));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 6;");
        return row;
    }
}
