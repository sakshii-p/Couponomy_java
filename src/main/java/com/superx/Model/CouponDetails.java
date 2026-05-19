package com.superx.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CouponDetails {

    private String buyerId;
    private String couponId;
    private String brandName;
    private String couponType;
    private String offerValue;
    private String couponCode;
    private String minOrdValue;
    private String category;
    private String expiry;
    private String description;
    private String sellerId;
    private String status;
    private String visibility;
    private int coinValue; // Replaces couponPrice
    private String couponSSURL;
    private String couponPosterURL;
    private String couponTitle;

    // ======= Getters and Setters =======

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getOfferValue() {
        return offerValue;
    }

    public void setOfferValue(String offerValue) {
        this.offerValue = offerValue;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getMinOrdValue() {
        return minOrdValue;
    }

    public void setMinOrdValue(String minOrdValue) {
        this.minOrdValue = minOrdValue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public int getCoinValue() {
        return coinValue;
    }

    public void setCoinValue(int coinValue) {
        this.coinValue = coinValue;
    }

    public String getCouponSSURL() {
        return couponSSURL;
    }

    public void setCouponSSURL(String couponSSURL) {
        this.couponSSURL = couponSSURL;
    }

    public String getCouponPosterURL() {
        return couponPosterURL;
    }

    public void setCouponPosterURL(String couponPosterURL) {
        this.couponPosterURL = couponPosterURL;
    }

    public int getValidityDaysLeft() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate expiryDate = LocalDate.parse(this.expiry, formatter);
            LocalDate today = LocalDate.now();
            long days = ChronoUnit.DAYS.between(today, expiryDate);
            return (int) days;
        } catch (Exception e) {
            System.err.println("Error parsing expiry date: " + e.getMessage());
            return 0;
        }
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    @Override
    public String toString() {
        return "CouponDetails{" +
                "buyerId='" + buyerId + '\'' +
                ", couponId='" + couponId + '\'' +
                ", brandName='" + brandName + '\'' +
                ", couponType='" + couponType + '\'' +
                ", offerValue='" + offerValue + '\'' +
                ", couponCode='" + couponCode + '\'' +
                ", minOrdValue='" + minOrdValue + '\'' +
                ", category='" + category + '\'' +
                ", expiry='" + expiry + '\'' +
                ", description='" + description + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", status='" + status + '\'' +
                ", visibility='" + visibility + '\'' +
                ", coinValue=" + coinValue +
                ", couponSSURL='" + couponSSURL + '\'' +
                ", couponPosterURL='" + couponPosterURL + '\'' +
                '}';
    }

}
