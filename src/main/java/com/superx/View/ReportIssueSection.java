package com.superx.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ReportIssueSection {

    public VBox getPage() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #F8F8FF;");
        root.setAlignment(Pos.TOP_CENTER);

        // ✅ Page Title
        Text title = new Text("Report an Issue");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setFill(Color.web("#B22222"));

        // ✅ Form
        VBox form = createReportForm();

        root.getChildren().addAll(title, form);
        return root;
    }

    private VBox createReportForm() {
        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject (e.g., Coupon not working)");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Describe your issue...");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(4);

        Button submitBtn = new Button("Submit Issue");
        submitBtn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        submitBtn.setTextFill(Color.WHITE);
        submitBtn.setCursor(Cursor.HAND);
        submitBtn.setStyle("-fx-background-color: #B22222; -fx-background-radius: 6;");
        submitBtn.setOnMouseEntered(
                e -> submitBtn.setStyle("-fx-background-color: derive(#B22222, 20%); -fx-background-radius: 6;"));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle("-fx-background-color: #B22222; -fx-background-radius: 6;"));
        submitBtn.setOnAction(e -> {
            System.out.println("Issue Reported: " + subjectField.getText() + " | " + descriptionArea.getText());
            subjectField.clear();
            descriptionArea.clear();
        });

        VBox form = new VBox(10, subjectField, descriptionArea, submitBtn);
        form.setPadding(new Insets(15));
        form.setPrefWidth(400);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        form.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.15)));

        return form;
    }
}
