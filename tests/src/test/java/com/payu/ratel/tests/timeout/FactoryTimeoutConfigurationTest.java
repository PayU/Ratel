package com.payu.ratel.tests.timeout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.payu.ratel.client.standalone.RatelClientFactory;
import com.payu.ratel.client.standalone.RatelStandaloneFactory;
import com.payu.ratel.config.TimeoutConfig;
import com.payu.ratel.tests.RatelTest;
import com.payu.ratel.tests.RatelTestContext;
import com.payu.ratel.tests.service.timeout.TimeoutService;
import com.payu.ratel.tests.service.timeout.TimeoutServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@RatelTest(registerServices = TimeoutServiceConfiguration.class)
public class FactoryTimeoutConfigurationTest {


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

        TimeoutService timeoutClient = clientFactory.getServiceProxy(TimeoutService.class, new TimeoutConfig(2000,
                2000));
        return timeoutClient;
    }

}
