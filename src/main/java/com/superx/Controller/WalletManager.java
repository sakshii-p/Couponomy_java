package com.superx.Controller;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.superx.Model.CouponDetails;

public class WalletManager {

    // Update the user's wallet by setting a new value (as String)
    public static void updateUserWallet(String userId, int newWalletCoins) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("userInfo").document(userId)
                .update("walletCoins", String.valueOf(newWalletCoins))
                .get();
    }

    // Add coins to seller by fetching current string value, converting to int, then
    // updating
    public static void addCoinsToSeller(String sellerId, int coins) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        DocumentSnapshot sellerDoc = db.collection("userInfo").document(sellerId).get().get();

        if (sellerDoc.exists() && sellerDoc.contains("walletCoins")) {
            String currentBalanceStr = sellerDoc.getString("walletCoins");
            int currentBalance = 0;
            try {
                currentBalance = Integer.parseInt(currentBalanceStr);
            } catch (NumberFormatException e) {
                currentBalance = 0; // default if parsing fails
            }

            int updatedBalance = currentBalance + coins;
            db.collection("userInfo").document(sellerId)
                    .update("walletCoins", String.valueOf(updatedBalance))
                    .get();
        }
    }

    // Update coupon info after purchase
    public static void updateCouponAfterPurchase(CouponDetails coupon) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("coupons").document(coupon.getCouponId())
                .update("status", coupon.getStatus(),
                        "visibility", coupon.getVisibility(),
                        "buyerId", coupon.getBuyerId())
                .get();
    }
}
