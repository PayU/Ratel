package com.payu.ratel.tests;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory.JBOSS_BIND_ADDRESS;
import static com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory.JBOSS_BIND_PORT;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.payu.ratel.Discover;
import com.payu.ratel.config.EnableServiceDiscovery;
import com.payu.ratel.config.ServiceDiscoveryConfig;
import com.payu.ratel.context.ProcessContext;
import com.payu.ratel.server.DiscoveryServerMain;
import com.payu.ratel.server.InMemoryDiscoveryServer;
import com.payu.ratel.tests.service.tracing.ProcessIdPassingService;
import com.payu.ratel.tests.service.tracing.ProcessIdTargetService;
import com.payu.ratel.tests.service.tracing.TracingTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ServiceDiscoveryConfig.class, DiscoveryServerMain.class})
@IntegrationTest({
        "server.port:8069",
        SERVICE_DISCOVERY_ADDRESS + ":http://localhost:8069/server/discovery"})
@WebAppConfiguration
@EnableServiceDiscovery
public class ProcessTracingTest {

    private ConfigurableApplicationContext remoteContext;

    @Autowired
    private InMemoryDiscoveryServer server;

    @Discover
    private ProcessIdPassingService	passingService;

    @Discover
    private ProcessIdTargetService	targetService;
    
    @Before
    public void before() throws InterruptedException {
        remoteContext = SpringApplication.run(TracingTestConfiguration.class,
                "--server.port=8031",
                "--" + JBOSS_BIND_ADDRESS + "=localhost",
                "--" + JBOSS_BIND_PORT + "=8031",
                "--spring.jmx.enabled=false",
                "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:8069/server/discovery");
    }

    @After
    public void close() {
        remoteContext.close();
    }

    
    @Test
    public void shouldGenerateProcessId() throws Exception {
    	await().atMost(10, TimeUnit.SECONDS).until(new Runnable() {
    		
    		@Override
    		public void run() {
    			assertThat(server.fetchAllServices()).hasSize(2);
    		}
    		
    	});
    	
    	//given
    	assertThat((targetService.getProcessId())).isNull();
    	
    	//when
    	passingService.passProcessId();
    	
    	//then
    	assertThat((targetService.getProcessId())).isNotNull();
    }
    
    public void shouldPassProcessId() throws Exception {
    	await().atMost(10, TimeUnit.SECONDS).until(new Runnable() {
    		
    		@Override
    		public void run() {
    			assertThat(server.fetchAllServices()).hasSize(2);
    		}
    		
    	});
    	
    	//given
    	assertThat((targetService.getProcessId())).isNull();
    	
    	//when
    	ProcessContext.getInstance().setProcessIdentifier("123");
    	passingService.passProcessId();
    	
    	//then
    	assertThat((targetService.getProcessId())).isEqualTo("123");
    	assertThat((passingService.getProcessId())).isEqualTo(targetService.getProcessId());
    }

}
