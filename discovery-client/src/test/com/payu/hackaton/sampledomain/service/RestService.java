package com.payu.hackaton.sampledomain.service;

import java.util.List;

public interface RestService {

    List<String> getOrders();
    String addOrder(String order);
}
