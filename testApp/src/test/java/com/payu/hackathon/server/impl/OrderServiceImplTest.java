package com.payu.hackathon.server.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

import com.payu.hackathon.server.model.Order;
import com.payu.hackathon.server.model.OrderBuilder;

public class OrderServiceImplTest {

    private OrderServiceImpl orderService = new OrderServiceImpl();

    @Test
    public void shouldSetWithId1() {
        //when
        orderService.createOrder(aBulder().withAmount("100").withWhat("Żelki").build());
        //then
        assertThat(orderService.getOrder(1L)).isNotNull();
    }

    @Test
    public void shouldReturnNextOne() {
        //given
        Order pedały = aBulder().withAmount("200").withWhat("Pedały").build();
        Order gacie = aBulder().withAmount("1000").withWhat("Złote gacie").build();
        //when
        orderService.createOrder(pedały);
        orderService.createOrder(gacie);
        //then
        then(orderService.getOrder(2L).getAmount()).isEqualTo("1000");
        then(orderService.getOrder(2L).getWhat()).isEqualTo("Złote gacie");
    }

    private OrderBuilder aBulder() {
        return new OrderBuilder();

    }
}
