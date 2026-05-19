package com.superx.Controller;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.superx.Model.CouponDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CouponManager {

    public static List<CouponDetails> getPurchasedCoupons(String userId) {
        List<CouponDetails> list = new ArrayList<>();
        try {
            Firestore db = FirestoreClient.getFirestore();
            List<QueryDocumentSnapshot> docs = db.collection("coupons")
                    .whereEqualTo("buyerId", userId)
                    .get().get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                list.add(doc.toObject(CouponDetails.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<CouponDetails> getListedCoupons(String userId) {
        List<CouponDetails> list = new ArrayList<>();
        try {
            Firestore db = FirestoreClient.getFirestore();
            List<QueryDocumentSnapshot> docs = db.collection("coupons")
                    .whereEqualTo("sellerId", userId)
                    .get().get().getDocuments();
            for (QueryDocumentSnapshot doc : docs) {
                list.add(doc.toObject(CouponDetails.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return list;
    }
}
