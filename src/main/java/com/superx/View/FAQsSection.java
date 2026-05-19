package com.superx.View;

import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class FAQsSection {

        public VBox getPage() {
                VBox root = new VBox(15);
                root.setPadding(new Insets(25));
                root.setStyle("-fx-background-color: #F8F8FF;");

                // Page Title
                Text title = new Text("Frequently Asked Questions");
                title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
                title.setFill(Color.web("#4B0082"));

                // Accordion for FAQs
                Accordion faqAccordion = new Accordion();
                faqAccordion.getPanes().addAll(
                                createFAQItem("How to use a purchased coupon?",
                                                "Go to 'My Coupons' → Click 'Use Now' → Show QR/Code to vendor."),
                                createFAQItem("How to sell my coupon?",
                                                "Go to 'Sell Coupon' page → Fill details → Wait for admin verification."),
                                createFAQItem("What if my coupon is not working?",
                                                "Contact support immediately. Refund will be processed if eligible."),
                                createFAQItem("How does admin verification work?",
                                                "All sold coupons undergo admin review to ensure validity."),
                                createFAQItem("Can I get a refund for expired coupons?",
                                                "Refunds are not allowed for expired coupons unless proven invalid."));

                root.getChildren().addAll(title, faqAccordion);
                return root;
        }

        private TitledPane createFAQItem(String question, String answer) {
                Text answerText = new Text(answer);
                answerText.setFont(Font.font("Segoe UI", 14));
                answerText.setFill(Color.web("#555"));

                VBox content = new VBox(answerText);
                content.setPadding(new Insets(10));

                TitledPane pane = new TitledPane(question, content);
                pane.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
                return pane;
        }
}
