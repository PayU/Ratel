package com.payu.hackathon.discovery.server;

import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServiceRegisterTest {

    private TestingServer zkTestServer;
    ServiceRegister serviceRegister;

    @Before
    public void startZookeeper() throws Exception {
        zkTestServer = new TestingServer(2181);

    }

    @After
    public void stopZookeeper() throws Exception {
        zkTestServer.stop();
    }

    @Test
    public void shouldRegisterServices() throws Exception {
        //given
        serviceRegister = new ServiceRegister("com.payu.hackathon.discovery.sampledomain.service",
                "localhost:8080/app", zkTestServer.getConnectString());
        //when
        serviceRegister.registerServices();
        //then
    }
}
