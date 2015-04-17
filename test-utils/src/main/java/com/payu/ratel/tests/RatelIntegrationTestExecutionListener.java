package com.payu.ratel.tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;

import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;

public class RatelIntegrationTestExecutionListener implements TestExecutionListener, Ordered{
  
  @Override
  public void beforeTestClass(TestContext testContext) throws Exception {
    
  }

  @Override
  public void prepareTestInstance(TestContext testContext) throws Exception {
    
    applyPropertiesFromAnnotation(testContext, IntegrationTest.class.getName());
    applyPropertiesFromAnnotation(testContext, RatelTest.class.getName());
    
    int servicePort = RatelTestContext.SERVICE_DISCOVERY_PORT;//hardcoded, can be changed to finding free tcp port 
    applyServiceRegistryProperties(testContext, servicePort);
  }

  private void applyServiceRegistryProperties(TestContext testContext, int servicePort) {
    String[] properties = {
        "server.port:" + servicePort,
        SERVICE_DISCOVERY_ADDRESS + ":http://localhost:" + servicePort + "/server/discovery"};
    addPropertySourceProperties(testContext, properties);
  }

  private void applyPropertiesFromAnnotation(TestContext testContext, String annotationName) {
    Class<?> testClass = testContext.getTestClass();
    if (AnnotatedElementUtils.isAnnotated(testClass, annotationName)) {
      AnnotationAttributes annotationAttributes = AnnotatedElementUtils
          .getAnnotationAttributes(testClass, annotationName);
      addPropertySourceProperties(testContext,
          annotationAttributes.getStringArray("value"));
    }
  }
  
  private void addPropertySourcePropertiesUsingReflection(TestContext testContext,
      String[] properties) throws Exception {
    MergedContextConfiguration configuration = (MergedContextConfiguration) ReflectionTestUtils
        .getField(testContext, "mergedContextConfiguration");
    Set<String> merged = new LinkedHashSet<String>((Arrays.asList(configuration
        .getPropertySourceProperties())));
    merged.addAll(Arrays.asList(properties));
    addIntegrationTestProperty(merged);
    ReflectionTestUtils.setField(configuration, "propertySourceProperties",
        merged.toArray(new String[merged.size()]));
  }

  private void addPropertySourceProperties(TestContext testContext, String[] properties) {
    try {
      addPropertySourcePropertiesUsingReflection(testContext, properties);
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  private void addIntegrationTestProperty(Collection<String> propertySourceProperties) {
    propertySourceProperties.add(IntegrationTest.class.getName() + "=true");
  }

  @Override
  public void beforeTestMethod(TestContext testContext) throws Exception {

    RatelTest ratelIntTestAnn = testContext.getTestClass().getAnnotation(RatelTest.class);
    RatelTestContext ratelTestCtx = getRatelTestContext(testContext);
    if (ratelIntTestAnn != null) {
      Class[] contextsToStart = ratelIntTestAnn.registerServices();
      
      for (Class class1 : contextsToStart) {
          ratelTestCtx.startService(class1);
      }
    }
    ratelTestCtx.waitForServicesRegistration();
  }

  private RatelTestContext getRatelTestContext(TestContext testContext) {
    RatelTestContext rtc = testContext.getApplicationContext().getBean(RatelTestContext.class);
    return rtc;
  }

  @Override
  public void afterTestMethod(TestContext testContext) throws Exception {
     RatelTestContext rtc = getRatelTestContext(testContext);
     rtc.close();
     
  }

  @Override
  public void afterTestClass(TestContext testContext) throws Exception {
  }

  @Override
  public int getOrder() {
    return Ordered.LOWEST_PRECEDENCE;
  }

}
