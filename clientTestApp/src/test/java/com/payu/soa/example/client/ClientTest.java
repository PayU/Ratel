package com.payu.soa.example.client;

import com.payu.server.model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(org.springframework.test.context.junit4.SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClientTestApp.class)
public class ClientTest {

    @Autowired
    private OrderServiceImpl testBean;

    @Test
    public void shouldCreateAndGetOrder() throws Exception {

        testBean.createOrder(new Order());
        final Order order = testBean.getOrder(1L);

        then(order).isNotNull();

    }
}
