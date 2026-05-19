package com.superx.View;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import com.superx.Dao.CouponInfoDAO;
import com.superx.Model.CouponDetails;

public class AdminPage extends Application {

    private VBox detailsPanel;
    private CouponDetails selectedCoupon;
    private List<CouponDetails> pendingCoupons = new ArrayList<>();
    private FlowPane pendingGrid;
    private Label pendingCount;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Panel - Coupon Verification");

        // HEADER
        HBox header = new HBox();
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #3b82f6;");
        Text title = new Text("Admin Dashboard");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        header.getChildren().add(title);

        // Stats
        HBox statsRow = new HBox(20);
        statsRow.setPadding(new Insets(20));
        statsRow.setAlignment(Pos.CENTER_LEFT);
        pendingCount = createStatCard("Pending", "#facc15");
        statsRow.getChildren().addAll(pendingCount);

        // TabPane
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        pendingGrid = createGrid();
        tabPane.getTabs().add(new Tab("Pending", createTabContent(pendingGrid)));

        // Details Panel
        detailsPanel = new VBox(10);
        detailsPanel.setPadding(new Insets(20));
        detailsPanel.setPrefWidth(400);
        detailsPanel.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #ddd;");
        detailsPanel.getChildren().add(new Text("Select a coupon to view details"));

        ScrollPane detailsScroll = new ScrollPane(detailsPanel);
        detailsScroll.setFitToWidth(true);

        // Load Pending Coupons
        try {
            pendingCoupons.clear();
            // Ensure CouponInfoDAO.couponDatabase is initialized before calling this
            if (CouponInfoDAO.couponDatabase != null) {
                pendingCoupons.addAll(CouponInfoDAO.getCouponsByStatus("Pending"));
            } else {
                 System.err.println("ERROR: CouponInfoDAO.couponDatabase is null in AdminPage.start()");
                 // Show an error alert to the user maybe?
                 showAlert("Error", "Database Connection Failed", "Could not connect to the database. Please restart the application.");
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to load pending coupons.");
            e.printStackTrace();
            showAlert("Error", "Load Failed", "Could not load pending coupons from the database.");
        }
        // Refresh grid *after* loading data
        refreshGrid(); // Error occurs here if createCouponCard fails

        // Layout
        VBox leftPane = new VBox(header, statsRow, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        HBox mainLayout = new HBox(10, leftPane, detailsScroll);
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, 1500, 850);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Label createStatCard(String title, String colorHex) {
        Label card = new Label(title + ": 0");
        card.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; "
                + "-fx-padding: 15; -fx-background-radius: 10; -fx-font-size: 16px; -fx-font-weight: bold;");
        return card;
    }

    private ScrollPane createTabContent(FlowPane grid) {
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private FlowPane createGrid() {
        FlowPane grid = new FlowPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));
        grid.setPrefWrapLength(900); // Adjust as needed based on leftPane width
        return grid;
    }

    // --- UPDATED METHOD ---
    private VBox createCouponCard(CouponDetails coupon, String status) {
        ImageView imageView = new ImageView(); // Create ImageView first
        imageView.setFitWidth(180);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        // --- NULL CHECK ADDED ---
        String imageUrl = coupon.getCouponSSURL(); // Get the URL String
        if (imageUrl != null && !imageUrl.trim().isEmpty()) { // Check if not null and not empty/whitespace
            try {
                // Only try to create Image if URL is valid
                Image couponImage = new Image(imageUrl);
                if (couponImage.isError()) { // Check if image loading failed
                     System.err.println("Error loading image (isError=true) for coupon card: " +
                            (coupon.getCouponId() != null ? coupon.getCouponId() : "UNKNOWN") +
                            " URL: [" + imageUrl + "]");
                     // Optionally set placeholder on error
                     // imageView.setImage(getPlaceholderImage());
                } else {
                    imageView.setImage(couponImage); // Set the image on the ImageView
                }
            } catch (IllegalArgumentException | NullPointerException e) {
                // Log error if URL is invalid or causes issues during loading
                System.err.println("Exception loading coupon card image for coupon: " +
                        (coupon.getCouponId() != null ? coupon.getCouponId() : "UNKNOWN") +
                        " URL: [" + imageUrl + "] Error: " + e.getMessage());
                // Optionally set a placeholder image here if loading fails
                // imageView.setImage(getPlaceholderImage());
            }
        } else {
            // Log if the URL field was null or empty in the first place
            System.out.println("DEBUG: No couponSSURL found for coupon card: " +
                    (coupon.getCouponId() != null ? coupon.getCouponId() : "UNKNOWN"));
            // Optionally set a placeholder image here
            // imageView.setImage(getPlaceholderImage());
        }
        // --- END OF NULL CHECK ---

        Text brandText = new Text(coupon.getBrandName() != null ? coupon.getBrandName() : "No Brand"); // Handle null brand name
        brandText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        brandText.setFill(Color.web("#334155"));

        Button viewDetailsBtn = new Button("View Details");
        viewDetailsBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white;");
        viewDetailsBtn.setOnAction(e -> showDetails(coupon));

        VBox card = new VBox(8, imageView, brandText, viewDetailsBtn); // imageView is now added here
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 3);");

        // Hover Animation (Unchanged)
        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

        return card;
    }
    // --- END OF UPDATED METHOD ---


    // --- UPDATED METHOD ---
    private void showDetails(CouponDetails coupon) {
        selectedCoupon = coupon;
        detailsPanel.getChildren().clear();

        if (coupon == null) {
             detailsPanel.getChildren().add(new Text("Error: Selected coupon data is null."));
             return;
        }

        Text title = new Text("Coupon Details");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        // Use helper method to safely get text, handling nulls
        VBox info = new VBox(
                createDetailText("Brand: ", coupon.getBrandName()),
                createDetailText("Offer: ", coupon.getOfferValue()),
                createDetailText("Min Order: ", coupon.getMinOrdValue()),
                createDetailText("Code: ", coupon.getCouponCode()),
                createDetailText("Category: ", coupon.getCategory()),
                createDetailText("Price: ₹", String.valueOf(coupon.getCoinValue())), // Assuming getCoinValue returns int/double
                createDetailText("Description: ", coupon.getDescription()),
                createDetailText("Expiry: ", coupon.getExpiry()),
                createDetailText("Status: ", coupon.getStatus()),
                createDetailText("Visibility: ", coupon.getVisibility()),
                createDetailText("Seller: ", coupon.getSellerId())
        );

        // --- Screenshot View with NULL CHECK ---
        ImageView screenshotView = new ImageView();
        screenshotView.setFitWidth(200);
        screenshotView.setPreserveRatio(true);
        String ssUrl = coupon.getCouponSSURL();
        if (ssUrl != null && !ssUrl.trim().isEmpty()) {
            try {
                Image img = new Image(ssUrl);
                if (img.isError()){
                     System.err.println("Error loading screenshot image (isError=true) in details: URL [" + ssUrl + "]");
                     // screenshotView.setImage(getPlaceholderImage());
                } else {
                     screenshotView.setImage(img);
                }
            } catch (Exception e) {
                System.err.println("Exception loading screenshot image in details: URL [" + ssUrl + "] Error: " + e.getMessage());
                // screenshotView.setImage(getPlaceholderImage());
            }
        } else {
             System.out.println("DEBUG: No couponSSURL for details view.");
             // screenshotView.setImage(getPlaceholderImage());
        }

        // --- Coupon Poster View with NULL CHECK ---
        ImageView couponImgView = new ImageView();
        couponImgView.setFitWidth(200);
        couponImgView.setPreserveRatio(true);
        String posterUrl = coupon.getCouponPosterURL();
         if (posterUrl != null && !posterUrl.trim().isEmpty()) {
            try {
                Image img = new Image(posterUrl);
                 if (img.isError()){
                     System.err.println("Error loading poster image (isError=true) in details: URL [" + posterUrl + "]");
                     // couponImgView.setImage(getPlaceholderImage());
                } else {
                     couponImgView.setImage(img);
                }
            } catch (Exception e) {
                System.err.println("Exception loading poster image in details: URL [" + posterUrl + "] Error: " + e.getMessage());
                // couponImgView.setImage(getPlaceholderImage());
            }
        } else {
             System.out.println("DEBUG: No couponPosterURL for details view.");
             // couponImgView.setImage(getPlaceholderImage());
        }

        Button approveBtn = new Button("✔ Approve");
        approveBtn.setStyle("-fx-background-color: #22c55e; -fx-text-fill: white;");
        approveBtn.setOnAction(e -> approveCoupon());

        Button rejectBtn = new Button("✖ Reject");
        rejectBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white;");
        rejectBtn.setOnAction(e -> rejectCoupon());

        HBox actionBtns = new HBox(10, approveBtn, rejectBtn);
        actionBtns.setPadding(new Insets(10, 0, 0, 0));

        detailsPanel.getChildren().addAll(title, info,
                new Text("Screenshot:"), screenshotView,
                new Text("Coupon Image:"), couponImgView, actionBtns);
    }
    // --- END OF UPDATED METHOD ---


    // --- UPDATED HELPER METHOD ---
    private Text createDetailText(String label, String value) {
        // Handle null values gracefully
        String displayValue = (value == null || value.trim().isEmpty()) ? "N/A" : value;
        Text t = new Text(label + displayValue);
        t.setWrappingWidth(360); // Ensure text wraps within the details panel width
        t.setFont(Font.font("Segoe UI", 14));
        t.setFill(Color.web("#374151"));
        return t;
    }
    // --- END OF UPDATED METHOD ---

    // --- Placeholder method - REPLACE with your actual placeholder ---
    // private Image getPlaceholderImage() {
    //     // Load a placeholder image from your resources
    //     try {
    //         return new Image(getClass().getResourceAsStream("/assets/images/placeholder.png"));
    //     } catch (Exception e) {
    //         System.err.println("Failed to load placeholder image.");
    //         return null; // Or return a default blank Image
    //     }
    // }
    // ---

    private void approveCoupon() {
        if (selectedCoupon != null) {
            try {
                CouponInfoDAO.updateCouponStatus(selectedCoupon.getCouponId(), "Approved", "public");
                pendingCoupons.remove(selectedCoupon);
                refreshGrid();
                clearDetailsPanel();
                 showAlert("Success", "Coupon Approved", "Coupon status updated to Approved and made public.");
            } catch (Exception e) {
                e.printStackTrace();
                 showAlert("Error", "Approval Failed", "Could not update coupon status.");
            }
        }
    }

    private void rejectCoupon() {
        if (selectedCoupon != null) {
            try {
                CouponInfoDAO.updateCouponStatus(selectedCoupon.getCouponId(), "Rejected", "private");
                pendingCoupons.remove(selectedCoupon);
                refreshGrid();
                clearDetailsPanel();
                 showAlert("Success", "Coupon Rejected", "Coupon status updated to Rejected and kept private.");
            } catch (Exception e) {
                e.printStackTrace();
                 showAlert("Error", "Rejection Failed", "Could not update coupon status.");
            }
        }
    }

    private void clearDetailsPanel() {
        selectedCoupon = null;
        detailsPanel.getChildren().clear();
        detailsPanel.getChildren().add(new Text("Select a coupon to view details"));
    }

    private void refreshGrid() {
        pendingGrid.getChildren().clear();
        if (pendingCoupons != null) { // Add null check for safety
            for (CouponDetails c : pendingCoupons) {
                 if (c != null) { // Add null check for safety
                    pendingGrid.getChildren().add(createCouponCard(c, "Pending"));
                 } else {
                      System.err.println("Warning: Found null coupon in pendingCoupons list during refreshGrid.");
                 }
            }
            pendingCount.setText("Pending: " + pendingCoupons.size());
        } else {
             System.err.println("ERROR: pendingCoupons list is null during refreshGrid.");
             pendingCount.setText("Pending: Error");
        }
    }

    // Helper method for showing alerts
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title.equalsIgnoreCase("Error")) {
            alert.setAlertType(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}