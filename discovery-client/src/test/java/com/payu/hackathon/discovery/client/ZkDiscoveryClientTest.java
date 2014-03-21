package com.payu.hackathon.discovery.client;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import com.payu.hackathon.discovery.model.Service;
import com.payu.hackathon.discovery.model.ServiceBuilder;
import com.payu.hackathon.discovery.server.ZkDiscoveryServer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class ZkDiscoveryClientTest {

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
    public void shouldFetchServices() throws Exception {
        //given
        ZkDiscoveryServer zkDiscoveryServer = new ZkDiscoveryServer(zkTestServer.getConnectString());
        ZkDiscoveryClient zkDiscoveryClient = new ZkDiscoveryClient(zkTestServer.getConnectString());
        Service service = ServiceBuilder.aService().
                withName("OrderService").
                withPath("/dupsko").
                withAddress("http://billpayments/dupsko").build();

        zkDiscoveryServer.registerService(service);

        //when
        Collection<Service> services = zkDiscoveryClient.fetchAllServices();

        //then
        assertThat(services.size()).isEqualTo(1);
        assertThat(services.stream().findFirst().get().getAddress()).isEqualTo("http://billpayments/dupsko");
        assertThat(services.stream().findFirst().get().getName()).isEqualTo("OrderService");
        assertThat(services.stream().findFirst().get().getPath()).isEqualTo("/dupsko");
    }

    @Test
    public void shouldListenForChangedService() throws Exception {
        //given
        ZkDiscoveryServer zkDiscoveryServer = new ZkDiscoveryServer(zkTestServer.getConnectString());
        ZkDiscoveryClient zkDiscoveryClient = new ZkDiscoveryClient(zkTestServer.getConnectString());
        Service service = ServiceBuilder.aService().
                withName("OrderService").
                withPath("/dupsko").
                withAddress("http://billpayments/dupsko").build();

        Service changedService = ServiceBuilder.aService().
                withName("OrderService").
                withPath("/noweDupsko").
                withAddress("http://billpayments/noweDupsko").build();

        zkDiscoveryServer.registerService(service);

        //when
        zkDiscoveryClient.listenForServices(newArrayList("OrderService"), updatedService -> {
            assertThat(updatedService.getAddress()).isEqualTo("http://billpayments/noweDupsko");
            assertThat(updatedService.getName()).isEqualTo("OrderService");
            assertThat(updatedService.getPath()).isEqualTo("/noweDupsko");
        });

        zkDiscoveryServer.registerService(changedService);
    }

}
