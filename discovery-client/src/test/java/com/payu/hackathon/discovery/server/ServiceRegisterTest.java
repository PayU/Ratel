package com.payu.hackathon.discovery.server;

import static org.assertj.core.api.Assertions.assertThat;

import com.payu.hackathon.discovery.client.ZkDiscoveryClient;
import com.payu.hackathon.discovery.model.Service;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class ServiceRegisterTest {

    private TestingServer zkTestServer;
    ServiceRegister serviceRegister;
    private CuratorFramework client;

    @Before
    public void startZookeeper() throws Exception {
        zkTestServer = new TestingServer(2181);
        client = CuratorFrameworkFactory.builder()
                .namespace(ZkDiscoveryServer.NAMESPACE)
                .connectString(zkTestServer.getConnectString())
                .retryPolicy(new RetryOneTime(2000))
                .build();
        client.start();

    }

    @After
    public void stopZookeeper() throws Exception {
        zkTestServer.stop();
    }

    @Test
    public void shouldRegisterServices() throws Exception {
        //given
        ZkDiscoveryClient zkDiscoveryClient = new ZkDiscoveryClient(zkTestServer.getConnectString());
        serviceRegister = new ServiceRegister("com.payu.hackathon.discovery.sampledomain.service",
                "localhost:8080/app", zkTestServer.getConnectString());
        //when
        serviceRegister.registerServices();
        //then
        Collection<Service> services = zkDiscoveryClient.fetchAllServices();
        Service service = services.stream().findFirst().get();
        assertThat(service).isNotNull();
        assertThat(service.getMethods()).hasSize(3);

    }
}
