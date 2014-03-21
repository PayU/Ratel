package com.payu.hackathon.server.model;

public class OrderBuilder {
    private String what;
    private String amount;

    public OrderBuilder withWhat(String what) {
        this.what = what;
        return this;
    }

    public OrderBuilder withAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public Order build() {
        return new Order(amount, what);
    }
}
