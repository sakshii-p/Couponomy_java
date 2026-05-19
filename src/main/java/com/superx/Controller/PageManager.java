package com.superx.Controller;

import java.util.concurrent.ExecutionException;

import com.superx.Model.CouponDetails;
import com.superx.View.AboutUs;
import com.superx.View.CartSection;
import com.superx.View.CouponDescription;
import com.superx.View.CouponPage;
import com.superx.View.LandingPage;
import com.superx.View.LoginPage;
import com.superx.View.RegisterPage;
import com.superx.View.SellerPage;
import com.superx.View.profilePage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.ArrayDeque;
import java.util.Deque;

public class PageManager {

    // inside PageManager
    private final Deque<Node> history = new ArrayDeque<>();

    private static Stage mainStage;
    private static PageManager instance;

    private PageManager() {
    };

    public static PageManager getInstance() {
        if (instance == null) {
            instance = new PageManager();
        }
        return instance;
    }

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public void showLoginPage() {
        try {
            LoginPage loginPage = new LoginPage();
            loginPage.start(mainStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showRegisterPage() {
        RegisterPage registerPage = new RegisterPage();
        Scene scene = new Scene(registerPage.registerPageHBox());
        mainStage.setScene(scene);
    }

    LandingPage landingPage = new LandingPage();

    public void showLandingPage() {
        Scene scene = new Scene(landingPage.createLandingPage());
        mainStage.setScene(scene);
    }

    public ScrollPane showMainLandingPage() {
        return LandingPage.mainScrollPane;
    }

    public void showProfileSection() {
        profilePage profilePage = new profilePage();
        LandingPage.rootLayout.setCenter(profilePage.createProfileHBox());
    }

    public void showCartSection() {
        CartSection cartSection = new CartSection();
        LandingPage.rootLayout.setCenter(cartSection.createCart());
    }

    public void showSellingPage() {
        SellerPage sellerPage = new SellerPage();
        LandingPage.rootLayout.setCenter(sellerPage.createSellingPage(mainStage));
    }

    public void showCouponListPage() {
        try {
            CouponPage couponPage = new CouponPage();
            LandingPage.rootLayout.setCenter(couponPage.createCouponPage());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();

            // Optional: Show alert dialog to user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to load coupons");
            alert.setContentText("There was a problem retrieving coupons from thedatabase.");
            alert.showAndWait();
        }
    }

    public void showCouponListPageWithSearch(String searchText) {
        try {
            CouponPage couponPage = new CouponPage();
            couponPage.setSearchQuery(searchText); // New method in CouponPage
            LandingPage.rootLayout.setCenter(couponPage.createCouponPage());
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to load coupons");
            alert.setContentText("There was a problem retrieving coupons.");
            alert.showAndWait();
        }
    }

    public void showCouponListPage(String category) {
        try {
            CouponPage couponPage = new CouponPage(category); // Pass category
            LandingPage.rootLayout.setCenter(couponPage.createCouponPage()); // Set directly to center
        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to load coupons");
            alert.setContentText("There was a problem retrieving coupons from the database.");
            alert.showAndWait();
        }
    }

    public void showCouponDescription(CouponDetails coupon) {
        CouponDescription couponDescription = new CouponDescription();
        setView(couponDescription.createCouponDescription(coupon));
    }

    public void setView(Node node) {
        // push current center to history (if any)
        if (LandingPage.rootLayout != null) {
            Node current = LandingPage.rootLayout.getCenter();
            if (current != null) {
                history.push(current);
            }
            LandingPage.rootLayout.setCenter(node);
        }
    }

    public void goBack() {
        if (!history.isEmpty()) {
            LandingPage.rootLayout.setCenter(history.pop());
        }
    }

    public void showAboutUsPage() {
        AboutUs aboutUs = new AboutUs();
        LandingPage.rootLayout.setCenter(aboutUs.showAboutUs());
    }

    // Other navigtion pages
}
