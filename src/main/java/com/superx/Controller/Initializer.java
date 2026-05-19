package com.superx.Controller;

import com.superx.View.LandingPage;
import com.superx.View.RegisterPage;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Initializer {
    
    public Scene initializeRegistrationPage(Stage primaryStage){
        RegisterPage registerPage =  new RegisterPage();
        registerPage.setRegisterPageStage(primaryStage);
        Scene registerScene = new Scene(registerPage.registerPageHBox());
        registerPage.setRegisterPageScene(registerScene);

        return registerScene;
    }

    public Scene initializeLandingPage(Stage primaryStage){
        LandingPage landingPage =  new LandingPage();
        landingPage.setLandingPageStage(primaryStage);
        Scene landingScene = new Scene(landingPage.createLandingPage());
        landingPage.setLandingPageScene(landingScene);

        return landingScene;
    }
}
