package com.superx.Dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.superx.Model.CouponDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CouponInfoDAO {

    public static Firestore couponDatabase;

    // Add Coupon with manual document ID
    public static void addCoupon(CouponDetails coupon) throws ExecutionException, InterruptedException {
        String couponId = coupon.getCouponId(); // Should already be set
        DocumentReference docRef = couponDatabase.collection("coupons").document(couponId);
        ApiFuture<WriteResult> result = docRef.set(coupon);
        result.get();
    }

    public static void updateCouponStatus(String couponId, String status, String visibility)
            throws ExecutionException, InterruptedException {
        DocumentReference docRef = couponDatabase.collection("coupons").document(couponId);
        docRef.update("status", status, "visibility", visibility).get();
    }

    // Generate next coupon ID in format CPN10001, CPN10002, ...
    public static String generateNextCouponId() throws ExecutionException, InterruptedException {
        CollectionReference collection = couponDatabase.collection("coupons");

        ApiFuture<QuerySnapshot> future = collection.orderBy("couponId", Query.Direction.DESCENDING).limit(1).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        int nextNumber = 10001; // Starting number if no coupons exist

        if (!documents.isEmpty()) {
            String lastCouponId = documents.get(0).getString("couponId"); // e.g. CPN10023
            if (lastCouponId != null && lastCouponId.startsWith("CPN")) {
                int lastNumber = Integer.parseInt(lastCouponId.substring(3)); // extract number part
                nextNumber = lastNumber + 1;
            }
        }

        return String.format("CPN%05d", nextNumber); // e.g. CPN10024
    }

    public static List<CouponDetails> getAllCoupons() throws ExecutionException, InterruptedException {
        List<CouponDetails> couponList = new ArrayList<>();

        ApiFuture<QuerySnapshot> query = couponDatabase.collection("coupons").get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            CouponDetails coupon = doc.toObject(CouponDetails.class);
            couponList.add(coupon);
        }

        return couponList;
    }

    public static List<CouponDetails> getCouponsByStatus(String status)
            throws ExecutionException, InterruptedException {
        List<CouponDetails> couponList = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = CouponInfoDAO.couponDatabase.collection("coupons")
                .whereEqualTo("status", status)
                .get();
        for (QueryDocumentSnapshot doc : query.get().getDocuments()) {
            CouponDetails coupon = doc.toObject(CouponDetails.class);
            couponList.add(coupon);
        }
        return couponList;
    }

}
