package com.superx.View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.superx.Dao.CouponInfoDAO;
import com.superx.Model.CouponDetails;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CouponPage {

    private GridPane couponGrid;

    // Filters coming from LandingPage
    private String selectedCategory;
    private String selectedBrand;

    // Local (left-rail) filters
    private String selectedExpiryFilter = "All"; // All | Next 7 Days | Next 30 Days | Expired
    private String selectedCouponType = "All"; // All | Flat | Percentage | Cashback
    private int maxCoinValue = Integer.MAX_VALUE; // current slider value
    private int absoluteMaxCoins = 1000; // computed from DB to size the slider

    // Optional search
    private String searchQuery = null;

    // Firestore date format we store/use
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Show everything (no category/brand constraints)
    public CouponPage() {
        this(null, null);
    }

    public CouponPage(String category) {
        this(category, null);
    }

    /** Pass brand with isBrand = true from the caller if needed */
    public CouponPage(String value, boolean isBrand) {
        if (isBrand) {
            this.selectedBrand = value;
        } else {
            this.selectedCategory = value;
        }
    }

    private CouponPage(String category, String brand) {
        this.selectedCategory = category;
        this.selectedBrand = brand;
    }

    public void setSearchQuery(String query) {
        this.searchQuery = (query == null || query.isBlank()) ? null : query.trim().toLowerCase();
    }

    public BorderPane createCouponPage() throws ExecutionException, InterruptedException {
        try {
            Class.forName("com.superx.Configuration.FireBaseInitializer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        BorderPane root = new BorderPane();

        // Center grid
        couponGrid = new GridPane();
        couponGrid.setHgap(30);
        couponGrid.setVgap(30);
        couponGrid.setPadding(new Insets(30));

        ScrollPane scrollPane = new ScrollPane(couponGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Left filter rail
        VBox filterSection = createFilterSection();
        root.setLeft(filterSection);
        root.setCenter(scrollPane);

        // First load
        updateCouponGrid();

        return root;
    }

    private VBox createFilterSection() throws ExecutionException, InterruptedException {
        VBox filterBox = new VBox(16);
        filterBox.setPadding(new Insets(20));
        filterBox.setPrefWidth(260);
        filterBox.setStyle("-fx-background-color:#f9f9f9; -fx-border-color:#ddd;");

        Label title = new Label("Filter Coupons");
        title.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");

        // Expiry filter
        Label expiryLabel = new Label("Expiry");
        ComboBox<String> expiryCombo = new ComboBox<>();
        expiryCombo.getItems().addAll("All", "Next 7 Days", "Next 30 Days", "Expired");
        expiryCombo.setValue(selectedExpiryFilter);
        expiryCombo.setOnAction(e -> {
            selectedExpiryFilter = expiryCombo.getValue();
            refreshGrid();
        });

        // Type filter
        Label typeLabel = new Label("Coupon Type");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("All", "Flat", "Percentage", "Cashback");
        typeCombo.setValue(selectedCouponType);
        typeCombo.setOnAction(e -> {
            selectedCouponType = typeCombo.getValue();
            refreshGrid();
        });

        // Max Coin slider
        Label coinLabel = new Label("Max Coin Price");

        // figure out the absolute max to size the slider nicely
        absoluteMaxCoins = computeMaxCoinValueFromDB();
        if (absoluteMaxCoins <= 0)
            absoluteMaxCoins = 1000;

        Slider coinSlider = new Slider(0, absoluteMaxCoins, absoluteMaxCoins);
        coinSlider.setShowTickLabels(true);
        coinSlider.setShowTickMarks(true);
        coinSlider.setMajorTickUnit(Math.max(50, absoluteMaxCoins / 4.0));
        coinSlider.setBlockIncrement(Math.max(10, absoluteMaxCoins / 20.0));

        Label coinValueLabel = new Label("≤ " + absoluteMaxCoins + " coins");
        coinSlider.valueProperty().addListener((obs, ov, nv) -> {
            maxCoinValue = nv.intValue();
            coinValueLabel.setText("≤ " + maxCoinValue + " coins");
            refreshGrid();
        });

        // Reset button
        Button reset = new Button("Reset Filters");
        reset.setOnAction(e -> {
            selectedExpiryFilter = "All";
            selectedCouponType = "All";
            maxCoinValue = absoluteMaxCoins;
            expiryCombo.setValue("All");
            typeCombo.setValue("All");
            coinSlider.setValue(absoluteMaxCoins);
            refreshGrid();
        });

        filterBox.getChildren().addAll(
                title,
                expiryLabel, expiryCombo,
                typeLabel, typeCombo,
                coinLabel, coinSlider, coinValueLabel,
                reset);
        filterBox.setAlignment(Pos.TOP_LEFT);
        return filterBox;
    }

    private void refreshGrid() {
        try {
            updateCouponGrid();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateCouponGrid() throws ExecutionException, InterruptedException {
        couponGrid.getChildren().clear();

        List<CouponDetails> all = CouponInfoDAO.getAllCoupons();

        all.sort((c1, c2) -> {
            LocalDate d1 = safeParseDate(c1.getExpiry());
            LocalDate d2 = safeParseDate(c2.getExpiry());
            if (d1 == null && d2 == null)
                return 0;
            if (d1 == null)
                return 1;
            if (d2 == null)
                return -1;
            return d1.compareTo(d2);
        });

        int col = 0, row = 0;
        for (CouponDetails c : all) {
            if (c == null)
                continue;

            if (!"Approved".equalsIgnoreCase(c.getStatus()))
                continue;
            if (!"public".equalsIgnoreCase(c.getVisibility()))
                continue;

            if (selectedCategory != null && !"See All".equalsIgnoreCase(selectedCategory)) {
                if (!safeEqualsIgnoreCase(c.getCategory(), selectedCategory))
                    continue;
            }

            if (selectedBrand != null && !selectedBrand.isBlank()) {
                if (!safeEqualsIgnoreCase(c.getBrandName(), selectedBrand))
                    continue;
            }

            if (!"All".equalsIgnoreCase(selectedCouponType)) {
                if (!safeEqualsIgnoreCase(c.getCouponType(), selectedCouponType))
                    continue;
            }

            if (c.getCoinValue() > maxCoinValue)
                continue;

            if (!matchesExpiryFilter(c))
                continue;

            if (searchQuery != null && !searchQuery.isBlank()) {
                String title = safeLower(c.getCouponTitle());
                String brand = safeLower(c.getBrandName());
                if (!title.contains(searchQuery) && !brand.contains(searchQuery))
                    continue;
            }

            String image = (c.getCouponPosterURL() != null && !c.getCouponPosterURL().isEmpty())
                    ? c.getCouponPosterURL()
                    : "https://via.placeholder.com/600x300?text=Coupon";

            String title = (c.getCouponTitle() != null && !c.getCouponTitle().isEmpty())
                    ? c.getCouponTitle()
                    : (c.getBrandName() != null ? c.getBrandName() : "Coupon");

            String brand = (c.getBrandName() != null) ? c.getBrandName() : "Unknown";

            VBox card = CouponCard.commonCouponCard(c);
            couponGrid.add(card, col, row);

            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
        }
    }

    private boolean matchesExpiryFilter(CouponDetails c) {
        if ("All".equalsIgnoreCase(selectedExpiryFilter))
            return true;
        LocalDate exp = safeParseDate(c.getExpiry());
        LocalDate today = LocalDate.now();

        if (exp == null)
            return false; // unknown expiry => don't show under constrained filters

        switch (selectedExpiryFilter) {
            case "Next 7 Days":
                return !exp.isBefore(today) && !exp.isAfter(today.plusDays(7));
            case "Next 30 Days":
                return !exp.isBefore(today) && !exp.isAfter(today.plusDays(30));
            case "Expired":
                return exp.isBefore(today);
            default:
                return true;
        }
    }

    private int computeMaxCoinValueFromDB() throws ExecutionException, InterruptedException {
        int max = 0;
        for (CouponDetails c : CouponInfoDAO.getAllCoupons()) {
            if (c == null)
                continue;
            max = Math.max(max, c.getCoinValue());
        }
        return max;
    }

    /* ------------ helpers ------------ */

    private static boolean safeEqualsIgnoreCase(String a, String b) {
        if (a == null || b == null)
            return false;
        return a.equalsIgnoreCase(b);
    }

    private static String safeLower(String s) {
        return s == null ? "" : s.toLowerCase();
    }

    private static LocalDate safeParseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank())
            return null;
        try {
            return LocalDate.parse(dateStr, DATE_FMT);
        } catch (Exception e) {
            try { // try ISO_LOCAL_DATE as fallback
                return LocalDate.parse(dateStr);
            } catch (Exception ignored) {
                return null;
            }
        }
    }
}
