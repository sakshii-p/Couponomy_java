package com.superx.Controller;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import java.util.concurrent.ExecutionException;

public class StatsManager {

    public static int getPurchasedCouponCount(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection("coupons")
                .whereEqualTo("buyerId", userId)
                .get().get().size();
    }

    public static int getSoldCouponCount(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        return db.collection("coupons")
                .whereEqualTo("sellerId", userId)
                .get().get().size();
    }
}
