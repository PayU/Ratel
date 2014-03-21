package com.payu.hackathon.discovery.sampledomain.service;

import java.util.List;

public interface RestService {

    List<String> getAllOrders();

    List<String> getUserOrders(String userId);

    String addOrder(String order);
}
