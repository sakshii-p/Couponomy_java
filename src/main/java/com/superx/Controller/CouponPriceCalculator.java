package com.superx.Controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CouponPriceCalculator {

    public static int calculateCouponCoinsForSeller(
            String brand,
            String couponType,
            String offerValueStr,
            String minOrderValueStr,
            String expiryDateStr,
            String category) {
        try {
            brand = brand.trim().toLowerCase();
            couponType = couponType.trim().toLowerCase();
            double offerValue = Double.parseDouble(offerValueStr.trim().replaceAll("[^\\d.]", ""));
            double minOrderValue = Double.parseDouble(minOrderValueStr.trim().replaceAll("[^\\d.]", ""));
            category = category.trim().toLowerCase();

            double benefit;
            switch (couponType) {
                case "flat":
                case "cashback":
                    benefit = offerValue;
                    break;
                case "percentage":
                    benefit = (offerValue / 100.0) * minOrderValue;
                    break;
                default:
                    benefit = 30;
            }

            double basePrice = (new Random().nextInt(6) + 10) / 100.0 * benefit;

            double adjustmentFactor = 1.0;
            int daysLeft = daysUntilExpiry(expiryDateStr);
            if (daysLeft < 5)
                adjustmentFactor -= 0.10;
            else if (daysLeft > 20)
                adjustmentFactor += 0.05;

            Set<String> trustedBrands = new HashSet<>(Arrays.asList(
                    "amazon", "zomato", "swiggy", "flipkart", "myntra", "bigbasket", "nykaa", "dominos", "ola",
                    "uber"));
            if (trustedBrands.contains(brand))
                adjustmentFactor += 0.05;

            Set<String> highDemandCategories = new HashSet<>(Arrays.asList(
                    "food", "grocery", "fashion", "travel"));
            if (highDemandCategories.contains(category))
                adjustmentFactor += 0.05;

            if (couponType.equals("cashback"))
                adjustmentFactor -= 0.05;

            double sellerEarning = Math.round(basePrice * adjustmentFactor * 100.0) / 100.0;
            return (int) Math.round(sellerEarning * 10); // ₹1 = 10 coins

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static int daysUntilExpiry(String expiryDate) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate expiry = LocalDate.parse(expiryDate);
            return (int) ChronoUnit.DAYS.between(today, expiry);
        } catch (Exception e) {
            return 10;
        }
    }
}
