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
import com.payu.ratel.Publish;
import com.payu.ratel.register.ServiceRegisterPostProcessor;
import com.payu.ratel.server.InMemoryDiscoveryServer;

public class RatelTestContext {

  private static final int REGISTRATION_TIMEOUT = 20;

  private static final int FREE_PORTS_START = 8021;

  @Autowired
  private InMemoryDiscoveryServer server;

  private int firstFreePort = FREE_PORTS_START;

  private List<ConfigurableApplicationContext> myContexts = new LinkedList<>();
  
  private List<ApplicationContext> observedContexts = new LinkedList<>();

  
  static int SERVICE_DISCOVERY_PORT = 18099;//hardcoded, in the future we can find free tcp port 

  @SuppressWarnings("rawtypes")
  public ConfigurableApplicationContext startService(Class springJavaConfigClasses) {
    ConfigurableApplicationContext newAppCtx = startNewApplication(firstFreePort, springJavaConfigClasses);
    firstFreePort++;
    return newAppCtx;
  }

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
  private ConfigurableApplicationContext createNewContext(int servicePort, Class springJavaConfigClass, int discoveryPort) {
    ConfigurableApplicationContext ctx = SpringApplication.run(springJavaConfigClass, "--server.port=" + servicePort,
        "--" + JBOSS_BIND_ADDRESS + "=localhost", "--" + JBOSS_BIND_PORT + "=" + servicePort,
        "--spring.jmx.enabled=false", "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:" + discoveryPort + "/server/discovery");
    return ctx;
  }

  public void waitForServicesRegistration(final int numberOfServices) {
    waitForRegistrationCondition(new Runnable() {

      @Override
      public void run() {
        assertThat(server.fetchAllServices()).hasSize(numberOfServices);
      }

    });
  }
  

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

  public boolean hasService(Class<?> serviceClass) {
    return server.hasService(serviceClass.getCanonicalName());
  }

  public void addObservedContext(ApplicationContext applicationContext) {
    observedContexts.add(applicationContext);
    
  }

}
