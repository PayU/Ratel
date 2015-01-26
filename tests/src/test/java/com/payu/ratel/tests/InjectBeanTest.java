package com.payu.ratel.tests;

import static com.payu.ratel.config.beans.RatelContextApplier.SERVICE_DISCOVERY_ENABLED;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.payu.ratel.Discover;
import com.payu.ratel.tests.service.TestService;
import com.payu.ratel.tests.service.TestServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestServiceConfiguration.class)
@IntegrationTest({
        SERVICE_DISCOVERY_ENABLED + ":false"
})
public class InjectBeanTest {

    @Discover
    private TestService testService;


    @Test
    public void shouldInjectService() throws InterruptedException {
        //given

        //when
        final String result = testService.hello();

        //then
        then(result).isEqualTo("success");
    }

}
