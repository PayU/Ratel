package com.payu.ratel.tests.timeout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.payu.ratel.Discover;
import com.payu.ratel.config.Timeout;
import com.payu.ratel.tests.RatelTest;
import com.payu.ratel.tests.service.timeout.AnnotatedTimeoutService;
import com.payu.ratel.tests.service.timeout.TimeoutServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@RatelTest(registerServices = TimeoutServiceConfiguration.class)
public class AnnotatedTimeoutConfigurationTest {

    @Discover
    @Timeout(readTimeout = 2000, connectTimeout = 2000)
    private AnnotatedTimeoutService timeoutService;

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

}
