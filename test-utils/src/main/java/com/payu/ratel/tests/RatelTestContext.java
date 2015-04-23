package com.payu.ratel.tests;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory.JBOSS_BIND_ADDRESS;
import static com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory.JBOSS_BIND_PORT;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.google.common.collect.Sets;
import com.payu.ratel.register.ServiceRegisterPostProcessor;
import com.payu.ratel.server.InMemoryDiscoveryServer;

/**
 * A context that is bound to a test with ratel-testing capabilities. It enables
 * you to control the process of registering/unregistering the services during
 * test execution.
 * 
 * @see {@link RatelTest}
 */
public class RatelTestContext {

  private static final int REGISTRATION_TIMEOUT = 78;

  private static final int FREE_PORTS_START = 8021;

  @Autowired
  private InMemoryDiscoveryServer server;

  private int firstFreePort = FREE_PORTS_START;

  private List<ConfigurableApplicationContext> myContexts = new LinkedList<>();

  private List<ApplicationContext> observedContexts = new LinkedList<>();

  static int SERVICE_DISCOVERY_PORT = 18099;// hardcoded, in the future we can
                                            // find free tcp port

  /**
   * Starts a new, separate spring context with a given configuration. The
   * started services have separate http port and are configured to use an
   * in-memory test registry server. If you start multiple services this way,
   * they will act as a separate applications, but will be registered in a
   * single registry service.
   * 
   * @param springJavaConfigClass
   *          the configuration to start. If you want to start multiple
   *          configurations in a single context, group them together in a
   *          single java config and pass in this parameter
   * @return the started context. You can close, restart or stop it during the
   *         test.
   */
  @SuppressWarnings("rawtypes")
  public ConfigurableApplicationContext startService(Class springJavaConfigClass) {
    ConfigurableApplicationContext newAppCtx = startNewApplication(firstFreePort, springJavaConfigClass);
    firstFreePort++;
    return newAppCtx;
  }

  /**
   * Closes all spring contexts that were created in
   */
  public void close() {
    for (ConfigurableApplicationContext ctx : myContexts) {
      ctx.close();
    }
    firstFreePort = FREE_PORTS_START;
    SERVICE_DISCOVERY_PORT++;
    myContexts.clear();
    observedContexts.clear();
  }

  @SuppressWarnings("rawtypes")
  private ConfigurableApplicationContext startNewApplication(int servicePort, Class springJavaConfigClasses) {
    ConfigurableApplicationContext ctx = createNewContext(servicePort, springJavaConfigClasses, SERVICE_DISCOVERY_PORT);

    myContexts.add(ctx);
    return ctx;
  }

  @SuppressWarnings("rawtypes")
  private ConfigurableApplicationContext createNewContext(int servicePort, Class springJavaConfigClass,
      int discoveryPort) {
    ConfigurableApplicationContext ctx = SpringApplication.run(springJavaConfigClass, "--server.port=" + servicePort,
        "--" + JBOSS_BIND_ADDRESS + "=localhost", "--" + JBOSS_BIND_PORT + "=" + servicePort,
        "--spring.jmx.enabled=false", "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:" + discoveryPort
            + "/server/discovery");
    return ctx;
  }

  /**
   * Wait until a test registry server bound to this context registers a given
   * number of active service instances.
   * 
   * @param numberOfServices
   *          the expected number of services
   * @throws AssertionError
   *           if the number was not reached within configured timeout
   */
  public void waitForServicesRegistration(final int numberOfServices) {
    waitForRegistrationCondition(new Runnable() {

      @Override
      public void run() {
        assertThat(server.fetchAllServices()).hasSize(numberOfServices);
      }

    });
  }

  /**
   * Wait until a test registry server bound to this context registers services
   * of all specified interfaces. When this method returns successfully, it is
   * guaranteed that all given services are up and running.
   * 
   * @param serviceInterfaces
   *          - array of expected service interfaces.
   * @throws AssertionError
   *           if at least one service was not registered within configured
   *           timeout
   */
  public void waitForServiceRegistration(Class<?>... serviceInterfaces) {
    final HashSet<Class<?>> notYetRegisteredServices = Sets.newHashSet(serviceInterfaces);

    waitForRegistrationCondition(new Runnable() {

      @Override
      public void run() {
        for (Iterator<Class<?>> it = notYetRegisteredServices.iterator(); it.hasNext();) {
          Class<?> serviceClass = it.next();
          if (server.hasService(serviceClass.getName())) {
            it.remove();
          }
        }
        assertThat(notYetRegisteredServices).isEmpty();
      }

    });

  }

  private void waitForRegistrationCondition(Runnable supplier) {
    await().atMost(REGISTRATION_TIMEOUT, TimeUnit.SECONDS).until(supplier);
  }

  /**
   * Wait until all services necessary for this test are up and running and
   * registered in the test registry server. The context assumes all contexts
   * given in {@link RatelTest#registerServices()} and explicitly added by
   * {@link RatelTestContext#addObservedContext(ApplicationContext)} to be
   * service producers. Therefore this method will not finalize until all
   * services published by the above contexts are available in the test registry
   * server.
   * 
   * @throws AssertionError
   *           if at least one of the services provided by relevant application
   *           contexts is not available in the service registry within the
   *           configured timeout.
   *
   */
  public void waitForServicesRegistration() {
    int expectedServices = 0;
    for (ApplicationContext ctx : myContexts) {
      expectedServices += getNumberOfRatelServicesInContext(ctx);
    }
    for (ApplicationContext ctx : observedContexts) {
      expectedServices += getNumberOfRatelServicesInContext(ctx);
    }
    waitForServicesRegistration(expectedServices);
  }

  private int getNumberOfRatelServicesInContext(ApplicationContext ctx) {
    ServiceRegisterPostProcessor ratelRegiseringPostProcessor = ctx.getBean(ServiceRegisterPostProcessor.class);
    if (ratelRegiseringPostProcessor == null) {
      return 0;
    }
    return ratelRegiseringPostProcessor.getRegisteredServices().size();
  }

  /**
   * Verifies if a test registry server bound to this context has registered at
   * least one instance of a service with a given contract.
   * 
   * @param serviceClass
   *          the service interface to check.
   * @return <code>true</code> iff the service is available
   */
  public boolean hasService(Class<?> serviceClass) {
    return server.hasService(serviceClass.getCanonicalName());
  }

  /**
   * Register a given context so that all Ratel services produced by it will be
   * observed by this {@link RatelTestContext}. If you invoke a method
   * {@link RatelTestContext#waitForServicesRegistration()}, the services from
   * this context will also be considered and the method will not finalize until
   * all services punlished by it are up and running.
   * 
   * @param applicationContext
   *          the context to add
   */
  public void addObservedContext(ApplicationContext applicationContext) {
    observedContexts.add(applicationContext);

  }

}
