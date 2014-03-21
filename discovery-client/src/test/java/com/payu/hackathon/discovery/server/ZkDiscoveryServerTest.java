package com.payu.hackathon.discovery.server;

import static org.assertj.core.api.Assertions.assertThat;

import com.payu.hackathon.discovery.model.Service;
import com.payu.hackathon.discovery.model.ServiceBuilder;
import com.payu.hackathon.discovery.model.ServiceSerializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

public class ZkDiscoveryServerTest {

    private TestingServer zkTestServer;

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
    public void shouldRegisterServiceInZk() throws Exception {
        //given
        ZkDiscoveryServer zkDiscoveryServer = new ZkDiscoveryServer(zkTestServer.getConnectString());
        ServiceSerializer serviceSerializer = new ServiceSerializer();
        Service service = ServiceBuilder.aService().
                withName("OrderService").
                withPath("/dupsko").
                withAddress(new URI("http://billpayments/dupsko")).build();

        //when
        zkDiscoveryServer.registerService(service);

        //then
        Service orderService = serviceSerializer.deserializeService(client.getData().forPath("OrderService"));
        assertThat(orderService.getAddress()).isEqualTo(new URI("http://billpayments/dupsko"));
        assertThat(orderService.getName()).isEqualTo("OrderService");
        assertThat(orderService.getPath()).isEqualTo("/dupsko");

    }

}
