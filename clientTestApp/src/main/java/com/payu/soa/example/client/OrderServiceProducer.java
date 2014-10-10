package com.payu.soa.example.client;


import com.payu.order.server.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class OrderServiceProducer {

    public static final int ORDERS_TO_CREATE = 10;

    private final TestBean testBean;

    @Autowired
    public OrderServiceProducer(TestBean testBean) {
        this.testBean = testBean;
    }

    private List<Order> orders = new ArrayList<>();

    @PostConstruct
    public void init() {
        IntStream.range(0, ORDERS_TO_CREATE).forEach(i -> orders.add(createOrder(i)));
    }

    @Scheduled(initialDelay = 1000, fixedRate = 500)
    public void cron() {
        orders.parallelStream().forEach(testBean::createOrderInAsync);
    }

    private Order createOrder(int i) {
        return new Order(new BigDecimal(Math.random()), "Foo " + i);
    }

}
