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
import org.springframework.context.ConfigurableApplicationContext;

import com.google.common.collect.Sets;
import com.payu.ratel.Publish;
import com.payu.ratel.server.InMemoryDiscoveryServer;

public class RatelTestContext {

  private static final int FREE_PORTS_START = 8021;

  @Autowired
  private InMemoryDiscoveryServer server;

  private int firstFreePort = FREE_PORTS_START;

  private List<ConfigurableApplicationContext> myContexts = new LinkedList<>();

  @SuppressWarnings("rawtypes")
  public void startService(Class springJavaConfigClasses) {
    startNewApplication(firstFreePort, springJavaConfigClasses);
    firstFreePort++;
  }

  public void close() {
    for (ConfigurableApplicationContext ctx : myContexts) {
      ctx.close();
    }
  }

  @SuppressWarnings("rawtypes")
  private void startNewApplication(int servicePort, Class springJavaConfigClasses) {
    ConfigurableApplicationContext ctx = createNewContext(servicePort, springJavaConfigClasses);

    myContexts.add(ctx);
    firstFreePort = FREE_PORTS_START;
  }

  @SuppressWarnings("rawtypes")
  private ConfigurableApplicationContext createNewContext(int servicePort, Class springJavaConfigClass) {
    ConfigurableApplicationContext ctx = SpringApplication.run(springJavaConfigClass, "--server.port=" + servicePort,
        "--" + JBOSS_BIND_ADDRESS + "=localhost", "--" + JBOSS_BIND_PORT + "=" + servicePort,
        "--spring.jmx.enabled=false", "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:8090/server/discovery");
    return ctx;
  }

  public void waitForServicesRegistration(final int numberOfServices) {
    await().atMost(20, TimeUnit.SECONDS).until(new Runnable() {

      @Override
      public void run() {
        assertThat(server.fetchAllServices()).hasSize(numberOfServices);
      }

    });
  }

  public void waitForServiceRegistration(Class<?>... serviceInterfaces) {
    final HashSet<Class<?>> notYetRegisteredServices = Sets.newHashSet(serviceInterfaces);

    await().atMost(20, TimeUnit.SECONDS).until(new Runnable() {

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

  public void waitForServicesRegistration() {
    int expectedServices = 0;
    for (ConfigurableApplicationContext ctx : myContexts) {
      expectedServices += getNumberOfRatelServicesInContext(ctx);
    }
    waitForServicesRegistration(expectedServices);
  }

  private int getNumberOfRatelServicesInContext(ConfigurableApplicationContext ctx) {
    return ctx.getBeanNamesForAnnotation(Publish.class).length;
  }

}
