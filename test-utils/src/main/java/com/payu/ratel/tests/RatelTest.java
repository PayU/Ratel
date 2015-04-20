package com.payu.ratel.tests;

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
    /*Default listeners from IntegrationTest 
     * This dirty hack works with older versions of spring, where TestExecutionListeners#mergeMode is not supported
     * Consequently, this ANNOTATION MUST BE USED BEFORE the @IntegrationTest annotation below
     */
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class, 
    SqlScriptsTestExecutionListener.class
    
})
@SpringApplicationConfiguration(classes={TestRatelConfiguration.class})
@IntegrationTest
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
