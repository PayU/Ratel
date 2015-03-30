package com.payu.ratel.tests;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory.JBOSS_BIND_ADDRESS;
import static com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory.JBOSS_BIND_PORT;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.payu.ratel.server.InMemoryDiscoveryServer;

public class TestContext {

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
	private ConfigurableApplicationContext createNewContext(int servicePort, Class springJavaConfigClasses) {
		ConfigurableApplicationContext ctx = SpringApplication.run(springJavaConfigClasses, "--server.port="
				+ servicePort, "--" + JBOSS_BIND_ADDRESS + "=localhost", "--" + JBOSS_BIND_PORT + "=" + servicePort,
				"--spring.jmx.enabled=false", "--" + SERVICE_DISCOVERY_ADDRESS
						+ "=http://localhost:8069/server/discovery");
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

	public void waitForServicesRegistration() {
		waitForServicesRegistration(myContexts.size());
	}

}
