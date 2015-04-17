package com.payu.ratel.tests;

import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.payu.ratel.config.EnableServiceDiscovery;


@Retention(RetentionPolicy.RUNTIME)
@WebAppConfiguration
@EnableServiceDiscovery
@TestExecutionListeners(listeners={RatelIntegrationTestExecutionListener.class, 
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class, 
    SqlScriptsTestExecutionListener.class})
@SpringApplicationConfiguration(classes={TestRatelConfiguration.class})
//@IntegrationTest({
//  "server.port:18069",
//  SERVICE_DISCOVERY_ADDRESS + ":http://localhost:18069/server/discovery"})
public @interface RatelTest {
  
  /**
   * 
   * @return the java configs that should be started before each test and stopped after each test 
   */
  public Class[] registerServices() default {};
  
  /**
   * Equivalent of {@link IntegrationTest#value()}
   */
  public String[] value() default {};

}
