package com.payu.ratel.tests;

import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * This test execution listener adds ratel testing capabilities to JUnit tests
 * annotated with {@link RatelTest} annotation. It does the following things:
 * <ul>
 * <li>It creates a bean of type {@link RatelTestContext} and places it in the
 * test context so that test implementation can inject it and use to manipulate
 * service registration process.</li>
 * <li>It starts a new in-memory registry server that will be used in the test</li>
 * <li>It guarantees that all services given in
 * {@link RatelTest#registerServices()} are registered and configured before
 * each test method and closed after each test method.
 * </ul>
 *
 */
public class RatelIntegrationTestExecutionListener extends AbstractTestExecutionListener {

  /**
   * Configures the context in such a way that the service registy server has a
   * new unique port, and all provided application contexts are configured to
   * use it. It also copies properites provided in {@link RatelTest#value()} and
   * {@link IntegrationTest#value()} annotations.
   *
   * @param testContext
   *          the TestContext of a test
   */
  @Override
  public void prepareTestInstance(TestContext testContext) {

    applyPropertiesFromAnnotation(testContext, IntegrationTest.class.getName());
    applyPropertiesFromAnnotation(testContext, RatelTest.class.getName());

    int servicePort = RatelTestContext.getServiceDiscoveryPort(); // hardcoded, can
                                                               // be changed to
                                                               // finding free
                                                               // tcp port
    applyServiceRegistryProperties(testContext, servicePort);
  }

  private void applyServiceRegistryProperties(TestContext testContext, int servicePort) {
    String[] properties = {"server.port:" + servicePort,
        SERVICE_DISCOVERY_ADDRESS + ":http://localhost:" + servicePort + "/server/discovery" };
    addPropertySourceProperties(testContext, properties);
  }

  private void applyPropertiesFromAnnotation(TestContext testContext, String annotationName) {
    Class<?> testClass = testContext.getTestClass();
    if (AnnotatedElementUtils.isAnnotated(testClass, annotationName)) {
      AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getAnnotationAttributes(testClass,
          annotationName);
      addPropertySourceProperties(testContext, annotationAttributes.getStringArray("value"));
    }
  }

  private void addPropertySourcePropertiesUsingReflection(TestContext testContext, String[] properties) {
    MergedContextConfiguration configuration = (MergedContextConfiguration) ReflectionTestUtils.getField(testContext,
        "mergedContextConfiguration");
    Set<String> merged = new LinkedHashSet<String>(Arrays.asList(configuration.getPropertySourceProperties()));
    merged.addAll(Arrays.asList(properties));
    addIntegrationTestProperty(merged);
    ReflectionTestUtils.setField(configuration, "propertySourceProperties", merged.toArray(new String[merged.size()]));
  }

  private void addPropertySourceProperties(TestContext testContext, String[] properties) {
      addPropertySourcePropertiesUsingReflection(testContext, properties);
  }

  private void addIntegrationTestProperty(Collection<String> propertySourceProperties) {
    propertySourceProperties.add(IntegrationTest.class.getName() + "=true");
  }

  /**
   * Starts all services given in {@link RatelTest#registerServices()} and waits
   * until they are successfully registered in the test registry server.
   *
   * @param testContext
   *          the TestContext of a test.
   *
   *
   * @see RatelTestContext#addObservedContext(org.springframework.context.ApplicationContext)
   *      @see RatelTestContext#startService(Class)
   *      @see RatelTestContext#waitForServicesRegistration()
   */
  @Override
  public void beforeTestMethod(TestContext testContext) {

    RatelTest ratelIntTestAnn = testContext.getTestClass().getAnnotation(RatelTest.class);
    RatelTestContext ratelTestCtx = getRatelTestContext(testContext);
    if (ratelIntTestAnn != null) {
      Class[] contextsToStart = ratelIntTestAnn.registerServices();

      for (Class class1 : contextsToStart) {
        ratelTestCtx.startService(class1);
      }
    }
    ratelTestCtx.addObservedContext(testContext.getApplicationContext());
    ratelTestCtx.waitForServicesRegistration();
  }

  private RatelTestContext getRatelTestContext(TestContext testContext) {
    return testContext.getApplicationContext().getBean(RatelTestContext.class);
  }

  /**
   * Closes all contexts registered in the {@link RatelTestContext} during test
   * method execution.
   *
   * @param testContext
   *          the TestContext of a test.
   *
   * @see RatelTestContext#addObservedContext(org.springframework.context.ApplicationContext)
   * @see RatelTestContext#startService(Class)
   * @see RatelTestContext#close()
   * @see RatelIntegrationTestExecutionListener#beforeTestMethod(TestContext)
   */
  @Override
  public void afterTestMethod(TestContext testContext) {
    RatelTestContext rtc = getRatelTestContext(testContext);
    rtc.close();

  }

}
