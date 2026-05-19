package com.superx.Controller;

import com.superx.Model.CouponDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CartManager {
    private static CartManager instance;
    private ObservableList<CouponDetails> items = FXCollections.observableArrayList();

    private CartManager() {
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public ObservableList<CouponDetails> getItems() {
        return items;
    }

    public void add(CouponDetails coupon) {
        if (!items.contains(coupon)) {
            items.add(coupon);
        }
    }

    public void clear() {
        items.clear();
    }
}
