package com.superx.View;

import com.superx.Model.UserSession;

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

public class WalletSection {

    private VBox transactionList;

    public VBox getPage() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #F8F8FF;");
        root.setAlignment(Pos.TOP_CENTER);

        // ✅ Page Title
        Text title = new Text("My Wallet");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setFill(Color.web("#4B0082"));

        // ✅ Balance Card
        VBox balanceCard = createBalanceCard(UserSession.getCurrentUser().getWalletCoins());

        // ✅ Filters & Transactions
        HBox filterButtons = createFilterButtons();
        transactionList = createTransactionHistory("All");

        // ✅ Rewards Section
        VBox rewardsSection = createRewardsSection();

        // ✅ Action Buttons
        HBox actionButtons = new HBox(15,
                createActionButton("Add Coins", "#4B0082"),
                createActionButton("Withdraw", "#B22222"));
        actionButtons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, balanceCard, filterButtons, transactionList, rewardsSection, actionButtons);
        return root;
    }

    // ✅ Balance Card
    private VBox createBalanceCard(String balance) {
        Text balanceTitle = new Text("Available Balance");
        balanceTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 16));
        balanceTitle.setFill(Color.web("#555"));

        Text balanceText = new Text(balance + " Coins");
        balanceText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        balanceText.setFill(Color.web("#4B0082"));

        VBox card = new VBox(5, balanceTitle, balanceText);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(250);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        card.setEffect(new DropShadow(6, Color.rgb(0, 0, 0, 0.15)));
        return card;
    }

    // ✅ Rewards Section
    private VBox createRewardsSection() {
        Text title = new Text("🏆 Rewards & Achievements");
        title.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 20));
        title.setFill(Color.web("#4B0082"));

        // Badges
        HBox badgesRow = new HBox(15,
                createBadge("Super Buyer", "#4B0082"),
                createBadge("Top Seller", "#388e3c"),
                createBadge("Referral King", "#f57c00"));
        badgesRow.setAlignment(Pos.CENTER_LEFT);

        // Progress to Next Reward
        Text nextRewardText = new Text("Earn 500 more coins to unlock 5% bonus!");
        nextRewardText.setFont(Font.font("Segoe UI", 14));
        nextRewardText.setFill(Color.web("#555"));

        ProgressBar progress = new ProgressBar(0.7);
        progress.setPrefWidth(300);
        progress.setStyle("-fx-accent: #4B0082;");

        VBox box = new VBox(10, title, badgesRow, nextRewardText, progress);
        box.setPadding(new Insets(15));
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        box.setEffect(new DropShadow(4, Color.rgb(0, 0, 0, 0.1)));

        return box;
    }

    private VBox createBadge(String name, String color) {
        Text badge = new Text("★");
        badge.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        badge.setFill(Color.web(color));

        Text nameText = new Text(name);
        nameText.setFont(Font.font("Segoe UI", 13));
        nameText.setFill(Color.web("#333"));

        VBox box = new VBox(2, badge, nameText);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(90);
        box.setPadding(new Insets(8));
        box.setStyle("-fx-background-color: #f9f9ff; -fx-background-radius: 8;");
        return box;
    }

    // ✅ Filter Buttons
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
        return btn;
    }

    private void updateTransactionList(String filter) {
        VBox parent = (VBox) transactionList.getParent();
        parent.getChildren().remove(transactionList);
        transactionList = createTransactionHistory(filter);
        parent.getChildren().add(3, transactionList);
    }

    // ✅ Transaction History
    private VBox createTransactionHistory(String filter) {
        Text title = new Text("Recent Transactions");
        title.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));
        title.setFill(Color.web("#333"));

        VBox list = new VBox(8);

        String[][] transactions = {
                { "Purchased Amazon Coupon", "-200 Coins", "#d32f2f", "✅" },
                { "Sold Flipkart Coupon", "+300 Coins", "#2e7d32", "✅" },
                { "Purchased Zomato Coupon", "-150 Coins", "#d32f2f", "⏳" },
                { "Admin Bonus", "+100 Coins", "#2e7d32", "✅" },
                { "Withdraw Request", "-500 Coins", "#d32f2f", "❌" }
        };

        for (String[] t : transactions) {
            boolean isCredit = t[1].startsWith("+");
            if (filter.equals("Credits") && !isCredit)
                continue;
            if (filter.equals("Debits") && isCredit)
                continue;
            list.getChildren().add(createTransactionItem(t[0], t[1], t[2], t[3]));
        }

        list.setPadding(new Insets(10));
        list.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        list.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.1)));

        return new VBox(10, title, list);
    }

    private HBox createTransactionItem(String detail, String amount, String color, String status) {
        Text detailText = new Text(detail);
        detailText.setFont(Font.font("Segoe UI", 14));
        detailText.setFill(Color.web("#555"));

        Text amountText = new Text(amount);
        amountText.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        amountText.setFill(Color.web(color));

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

        HBox row = new HBox(10, detailText, spacer, amountText, statusText);
        row.setPadding(new Insets(5, 10, 5, 10));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 5;");
        return row;
    }

    // ✅ Action Buttons
    private Button createActionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        btn.setTextFill(Color.WHITE);
        btn.setCursor(Cursor.HAND);
        btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8;");
        return btn;
    }
}
