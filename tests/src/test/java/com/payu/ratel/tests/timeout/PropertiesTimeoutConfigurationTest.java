package com.payu.ratel.tests.timeout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.payu.ratel.Discover;
import com.payu.ratel.client.standalone.RatelClientFactory;
import com.payu.ratel.client.standalone.RatelStandaloneFactory;
import com.payu.ratel.tests.RatelTest;
import com.payu.ratel.tests.RatelTestContext;
import com.payu.ratel.tests.service.timeout.TimeoutService;
import com.payu.ratel.tests.service.timeout.TimeoutServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@RatelTest(registerServices = TimeoutServiceConfiguration.class)
public class PropertiesTimeoutConfigurationTest {

    private String originalProp;

    @Discover
    private TimeoutService timeoutService;

    @Before
    public void setup() {
        this.originalProp = System.getProperty("ratel.readTimeout");
        System.setProperty("ratel.readTimeout", "2000");
    }

    @After
    public void tearDown() {
        System.setProperty("ratel.readTimeout", this.originalProp == null ? "" : this.originalProp);
    }

    @Test
    public void shouldNotTimeoutAfter1sWithInjectedClient() throws Exception {

        // when then
        assertThat(timeoutService.helloAfter1s()).isEqualTo("hello");
    }

    @Test(expected = RemoteConnectFailureException.class)
    public void shouldTimeoutBefore5sWithInjectedClient() throws Exception {
        // when
        timeoutService.helloAfter5s();

        // then
        fail("Timeout should have caused exception");
    }

    @Test
    public void shouldNotTimeoutAfter1sWithStandaloneClient() throws Exception {

        // given
        TimeoutService timeoutClient = getStandaloneTimeoutClient();

        // when then
        assertThat(timeoutClient.helloAfter1s()).isEqualTo("hello");
    }

    @Test(expected = RemoteConnectFailureException.class)
    public void shouldTimeoutBefore5sWithStandaloneClient() throws Exception {

        // given
        TimeoutService timeoutClient = getStandaloneTimeoutClient();

        // when
        timeoutClient.helloAfter5s();

        // then
        fail("Timeout should have caused exception");
    }

    private TimeoutService getStandaloneTimeoutClient() {
        String ratelAddr = "http://127.0.0.1:" + RatelTestContext.getServiceDiscoveryPort() + "/server/discovery";
        RatelClientFactory clientFactory = RatelStandaloneFactory.fromRatelServer(ratelAddr);

        TimeoutService timeoutClient = clientFactory.getServiceProxy(TimeoutService.class);
        return timeoutClient;
    }

}
