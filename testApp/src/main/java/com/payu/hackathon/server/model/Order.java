package com.payu.hackathon.server.model;

public class Order {
    private final String what;
    private final String amount;

    public Order(String amount, String what) {
        this.amount = amount;
        this.what = what;
    }

    public String getWhat() {
        return what;
    }

    public String getAmount() {
        return amount;
    }

}
