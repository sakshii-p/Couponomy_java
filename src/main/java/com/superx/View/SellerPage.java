package com.superx.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import com.superx.Controller.CouponPriceCalculator;
import com.superx.Controller.PageManager;
import com.superx.Dao.CouponInfoDAO;
import com.superx.Dao.FirebaseImage;
import com.superx.Model.CouponDetails;
import com.superx.Model.UserSession;

public class SellerPage {

    private String currentSymbol = "";
    private String storedBrand;
    private String storedCouponType;
    private String storedOfferValue;
    private String storedMinOrder;
    private String storedCouponCode;
    private String storedCategory;
    private String storedDescription;

    public StackPane createSellingPage(Stage primaryStage) {

        // Background image
        BackgroundImage bgImage = new BackgroundImage(
                new Image("assets/images/sellbg.png", 1600, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(1600, 800, false, false, false, false));

        Text sellTitle = new Text("Sell Your Coupon");
        sellTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        sellTitle.setFill(Color.web("#1f2937"));

        // Brand ComboBox + Custom Field
        Label brandLabel = new Label("Brand Name");
        ComboBox<String> brandBox = new ComboBox<>();
        brandBox.getItems().addAll("Amazon", "Flipkart", "Zomato", "Swiggy", "Myntra",
                "Ajio", "Domino's", "Uber", "Ola", "Nykaa",
                "BigBasket", "Blinkit", "Tata Neu", "Meesho", "Snapdeal",
                "BookMyShow", "MakeMyTrip", "Goibibo", "IRCTC", "Zepto",
                "1mg", "NetMeds", "PharmEasy", "Dunzo", "Paytm");
        brandBox.setPromptText("Select brand");
        brandBox.setPrefWidth(350);
        brandBox.setStyle(comboBoxStyle());

        Label cpTitleLabel = new Label("Coupon Title");
        TextField cpTitleField = new TextField();
        cpTitleField.setPromptText("Enter Coupon Title");
        cpTitleField.setPrefWidth(350);
        cpTitleField.setStyle(textFieldStyle());

        // Coupon Type
        Label couponTypeLabel = new Label("Coupon Type");
        ComboBox<String> couponTypeBox = new ComboBox<>();
        couponTypeBox.getItems().addAll("Flat", "Percentage", "Cashback");
        couponTypeBox.setPromptText("Select coupon type");
        couponTypeBox.setPrefWidth(350);
        couponTypeBox.setStyle(comboBoxStyle());

        // Offer Value
        Label offerValueLabel = new Label("Discount Amount (₹ or %)");
        TextField offerValueField = new TextField();
        offerValueField.setPromptText("Enter Discount Amount");
        offerValueField.setPrefWidth(350);
        offerValueField.setStyle(textFieldStyle());

        offerValueField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!currentSymbol.isEmpty()) {
                if (!newVal.matches("\\d*[" + currentSymbol + "]?")) {
                    newVal = newVal.replaceAll("[^\\d]", "");
                }
                if (!newVal.isEmpty() && !newVal.contains(currentSymbol)) {
                    offerValueField.setText(currentSymbol.equals("₹") ? "₹" + newVal : newVal + currentSymbol);
                }
            } else {
                offerValueField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        // Min Order
        Label minOrdLabel = new Label("Min. Order Value");
        TextField minOrderField = new TextField();
        minOrderField.setPromptText("Enter minimum order value (₹)");
        minOrderField.setPrefWidth(350);
        minOrderField.setStyle(textFieldStyle());

        // Coupon Code
        Label codeLabel = new Label("Coupon Code");
        TextField codeField = new TextField();
        codeField.setPromptText("Enter Coupon code");
        codeField.setPrefWidth(350);
        codeField.setStyle(textFieldStyle());

        // Category
        Label categoryLabel = new Label("Category");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Food & Beverages", "Grocery", "Fashion", "Electronics",
                "Beauty & Personal Care", "Medicines & Health", "Travel", "Entertainment",
                "Online Shopping", "Home & Kitchen", "Recharge & Bills", "Subscription Services");
        categoryBox.setPromptText("Select category");
        categoryBox.setPrefWidth(350);
        categoryBox.setStyle(comboBoxStyle());

        // Expiry
        Label expireLabel = new Label("Expire Date");
        DatePicker expiryPicker = new DatePicker();
        expiryPicker.setPromptText("DD/MM/YYYY");
        expiryPicker.setPrefWidth(350);
        expiryPicker.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 10;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                        "-fx-control-inner-background: rgba(255,255,255,0.2);");
        expiryPicker.getEditor().setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);");
        // Description
        Label descriptionLabel = new Label("Coupon Details");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Write coupon details (max 200 chars)");
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setPrefWidth(350);
        descriptionArea.setWrapText(true);
        descriptionArea.setStyle(
                "-fx-background-color: transparent;" + // Outer background
                        "-fx-control-inner-background: transparent;" + // Inner content
                        "-fx-background-insets: 0;" + // Remove padding
                        "-fx-text-fill: black;" +
                        "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;" +
                        "-fx-font-size: 14px;");

        // Screenshot Upload
        Label screenshotLabel = new Label("Upload ScreenShot");
        ImageView screenshotPreview = new ImageView();
        screenshotPreview.setFitWidth(200);
        screenshotPreview.setFitHeight(120);
        screenshotPreview.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5px;");

        Button uploadScreenshotBtn = new Button("Upload Screenshot");
        stylePrimaryButton(uploadScreenshotBtn);

        String[] screenshotUrl = new String[1]; // To store the uploaded image URL if needed later

        uploadScreenshotBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Choose Coupon Screenshot");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = fc.showOpenDialog(primaryStage);

            if (file != null) {
                screenshotPreview.setImage(new Image(file.toURI().toString()));

                // Create a unique filename for storage
                String fileName = "coupon_screenshot/" + System.currentTimeMillis() + "_" + file.getName();

                // Upload to Firebase Storage
                String imageUrl = FirebaseImage.uploadImage(file.getAbsolutePath(), fileName);

                if (imageUrl != null) {
                    screenshotUrl[0] = imageUrl;
                    System.out.println("Image uploaded successfully.");
                } else {
                    System.out.println("Image upload failed.");
                }
            }
        });

        // Coupon Image Upload
        Label couponImgLabel = new Label("Upload Coupon's Poster");
        ImageView couponImageView = new ImageView();
        couponImageView.setFitWidth(200);
        couponImageView.setFitHeight(120);
        couponImageView.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5px;");

        Button uploadCouponImageBtn = new Button("Upload Coupon's Poster");
        stylePrimaryButton(uploadCouponImageBtn);

        String[] uploadedImageUrl = new String[1]; // To store the uploaded image URL if needed later

        uploadCouponImageBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Choose Coupon Poster");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = fc.showOpenDialog(primaryStage);

            if (file != null) {
                couponImageView.setImage(new Image(file.toURI().toString()));

                // Create a unique filename for storage
                String fileName = "coupon_poster/" + System.currentTimeMillis() + "_" + file.getName();

                // Upload to Firebase Storage
                String imageUrl = FirebaseImage.uploadImage(file.getAbsolutePath(), fileName);

                if (imageUrl != null) {
                    uploadedImageUrl[0] = imageUrl;
                    System.out.println("Image uploaded successfully.");
                } else {
                    System.out.println("Image upload failed.");
                }
            }
        });

        // Confirmation checkbox
        CheckBox confirmationCheckBox = new CheckBox("I confirm this coupon is real and unused.");
        confirmationCheckBox.setTextFill(Color.web("#374151"));
        confirmationCheckBox.setFont(Font.font("Segoe UI", 14));

        Label estimatedPriceLabel = new Label();
        estimatedPriceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2563eb;");
        estimatedPriceLabel.setAlignment(Pos.CENTER);
        estimatedPriceLabel.setMaxWidth(Double.MAX_VALUE);
        // Buttons
        Button continueButton = new Button("Continue");
        stylePrimaryButton(continueButton);

        Button estimatedPriceBtn = new Button();
        estimatedPriceBtn.setVisible(false);
        estimatedPriceBtn.setManaged(false);
        estimatedPriceBtn.setStyle(
                "-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        estimatedPriceBtn.setMaxWidth(Double.MAX_VALUE);

        continueButton.setOnAction(e -> {
            if (brandBox.getValue() == null || couponTypeBox.getValue() == null ||
                    codeField.getText().isEmpty() || expiryPicker.getValue() == null ||
                    offerValueField.getText().isEmpty() || minOrderField.getText().isEmpty() ||
                    categoryBox.getValue() == null) {
                estimatedPriceLabel.setText("Please fill all required fields");
                return;
            }

            // Use either selected or custom brand
            String brand = brandBox.getValue();
            String couponType = couponTypeBox.getValue();
            String offer = offerValueField.getText();
            String minOrder = minOrderField.getText();
            String expiry = expiryPicker.getValue().toString();
            String category = categoryBox.getValue();

            int coins = CouponPriceCalculator.calculateCouponCoinsForSeller(
                    brand, couponType, offer, minOrder, expiry, category);

            estimatedPriceLabel.setText("Estimated Price: " + coins);

            storedBrand = brand;
            storedCouponType = couponType;
            storedOfferValue = offer;
            storedMinOrder = minOrder;
            storedCouponCode = codeField.getText();
            storedCategory = category;
            storedDescription = descriptionArea.getText();

            estimatedPriceBtn.setVisible(true);
            estimatedPriceBtn.setManaged(true);
        });

        Button sellNowBtn = new Button("Sell Now");
        sellNowBtn.setStyle(
                "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");
        sellNowBtn.setMaxWidth(250);
        sellNowBtn.setOnAction(event -> {
            try {
                CouponDetails couponDetails = new CouponDetails();

                // Required values already collected and validated
                couponDetails.setBrandName(storedBrand);
                couponDetails.setCouponType(storedCouponType);
                couponDetails.setOfferValue(storedOfferValue);
                couponDetails.setMinOrdValue(storedMinOrder);
                couponDetails.setCategory(storedCategory);
                couponDetails.setCouponCode(storedCouponCode);
                couponDetails.setDescription(storedDescription);
                couponDetails.setExpiry(expiryPicker.getValue().toString());
                couponDetails.setCouponSSURL(screenshotUrl[0]);
                couponDetails.setCouponPosterURL(uploadedImageUrl[0]);

                // Dynamically calculate and set price
                int coins = CouponPriceCalculator.calculateCouponCoinsForSeller(
                        storedBrand, storedCouponType, storedOfferValue,
                        storedMinOrder, expiryPicker.getValue().toString(), storedCategory);
                couponDetails.setCoinValue(coins);

                // 🔽 New fields set below
                couponDetails.setBuyerId(""); // Empty initially (not yet bought)
                couponDetails.setCouponTitle(cpTitleField.getText());
                String newCouponId = CouponInfoDAO.generateNextCouponId();
                couponDetails.setCouponId(newCouponId);
                couponDetails.setSellerId(UserSession.getCurrentUser().getUserId());
                couponDetails.setStatus("Pending");
                couponDetails.setVisibility("Private"); // Default value; adjust as needed

                // 🔥 Save to Firebase Firestore
                CouponInfoDAO.addCoupon(couponDetails); // ✅

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Coupon Submitted Successfully");
                alert.setContentText("Your coupon has been submitted for approval.");
                alert.showAndWait();

            } catch (Exception ex) {
                ex.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Coupon submission failed");
                errorAlert.setContentText("Something went wrong while submitting your coupon.");
                errorAlert.showAndWait();
            }

            PageManager.getInstance().showLandingPage();
        });

        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(20);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(30));

        int row = 0;

        grid.add(sellTitle, 0, row++, 2, 1);

        grid.add(new VBox(2, brandLabel, brandBox), 0, row);
        grid.add(new VBox(2, couponTypeLabel, couponTypeBox), 1, row++);

        // Row 3: Custom brand field and Offer Value
        grid.add(new VBox(2, offerValueLabel, offerValueField), 0, row);
        grid.add(new VBox(2, cpTitleLabel, cpTitleField), 1, row++);

        // Row 4: Min Order and Coupon Code
        grid.add(new VBox(2, minOrdLabel, minOrderField), 0, row);
        grid.add(new VBox(2, codeLabel, codeField), 1, row++);

        // Row 5: Category and Expiry
        grid.add(new VBox(2, categoryLabel, categoryBox), 0, row);
        grid.add(new VBox(2, expireLabel, expiryPicker), 1, row++);

        // Row 6: Description (spans across 2 columns)
        grid.add(new VBox(2, descriptionLabel, descriptionArea), 0, row++, 2, 1);

        // Row 7: Estimated Price label + Continue button in same row
        HBox estimateAndContinueBox = new HBox(20); // spacing between them
        estimateAndContinueBox.setAlignment(Pos.CENTER_RIGHT);

        estimatedPriceLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(estimatedPriceLabel, Priority.ALWAYS);

        estimateAndContinueBox.getChildren().addAll(continueButton, estimatedPriceLabel);
        grid.add(estimateAndContinueBox, 0, row++, 2, 1);

        // Row 7: Screenshot and Coupon Image
        grid.add(new VBox(2, screenshotLabel, uploadScreenshotBtn, screenshotPreview), 0, row);
        grid.add(new VBox(2, couponImgLabel, uploadCouponImageBtn, couponImageView), 1, row++);

        // Row 8: Confirmation checkbox (centered across 2 columns)
        grid.add(confirmationCheckBox, 0, row++, 2, 1);

        grid.add(sellNowBtn, 0, row++, 2, 1);

        // Wrap form in card-style VBox
        VBox formWrapper = new VBox(grid);
        formWrapper.setAlignment(Pos.TOP_CENTER);
        formWrapper.setPadding(new Insets(30)); // Added padding
        formWrapper.setSpacing(15);
        formWrapper.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.5);" +
                        "-fx-border-width: 1.5;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");

        formWrapper.setEffect(new GaussianBlur(20));

        // Use ScrollPane to avoid content overflow
        ScrollPane scrollPane = new ScrollPane(formWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setPrefViewportWidth(1000); // width inside scroll area
        scrollPane.setPrefViewportHeight(700); // keep within 850px stage height
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        StackPane contentWrapper = new StackPane(scrollPane);
        contentWrapper.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.setBackground(new Background(bgImage));
        root.getChildren().add(contentWrapper);
        return root;
    }

    private void stylePrimaryButton(Button btn) {
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btn.setStyle("-fx-background-color: rgba(59,130,246,0.8);" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 8;");

        btn.setPrefHeight(45);
        btn.setMaxWidth(350);
    }

    private String textFieldStyle() {
        return "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                "-fx-text-fill: black;" +
                "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(255,255,255,0.4);" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-font-size: 14px;";
    }

    private String comboBoxStyle() {
        return "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                "-fx-control-inner-background: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: rgba(255,255,255,0.4);" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 10;" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: black;" +
                "-fx-prompt-text-fill: rgba(40, 40, 40, 0.6);";
    }

}
