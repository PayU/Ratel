package com.payu.hackathon.overseer;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class OverseerServiceTest {

    ConfigurableApplicationContext context;
    TestingServer testingServer;

    @Before
    public void setup() throws Exception {
        testingServer = new TestingServer(2181);
        System.getProperties().put("service.zookeeper.connectionUrl", testingServer.getConnectString());

        Future<ConfigurableApplicationContext> future = Executors.newSingleThreadExecutor().submit(
                () -> SpringApplication.run(Application.class)
        );
        context = future.get(60, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws Exception {
        if (context != null) {
            context.close();
        }
        testingServer.close();
    }

    @Test
    public void testName() throws Exception {

        ResponseEntity entity = new RestTemplate().getForEntity("http://localhost:8081", String.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(entity.getBody().toString()).contains("links");

    }
}
