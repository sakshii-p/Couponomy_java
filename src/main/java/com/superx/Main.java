package com.superx;

import java.util.concurrent.ExecutionException;

import com.superx.Configuration.FireBaseInitializer;
import com.superx.View.LoginPage;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        System.out.println("Hello Couponomy");
        FireBaseInitializer fireBaseInitializer = new FireBaseInitializer();
        Application.launch(LoginPage.class, args);
        // System.out.println(CouponInfoDAO.getAllCoupons());
    }
}
