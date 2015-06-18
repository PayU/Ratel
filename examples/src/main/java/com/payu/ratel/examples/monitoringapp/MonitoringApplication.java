package com.payu.ratel.examples.monitoringapp;

import com.payu.ratel.config.EnableServiceDiscovery;
import com.payu.ratel.config.beans.RegistryStrategiesProvider;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableServiceDiscovery
@Component
@SuppressWarnings({"PMD.SignatureDeclareThrowsException", "PMD.SystemPrintln", "PMD.AvoidPrintStackTrace", "checkstyle:hideutilityclassconstructor"})
public class MonitoringApplication {

    @Autowired
    private RegistryStrategiesProvider strategiesProvider;

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(MonitoringApplication.class);
        app.setWebEnvironment(false);
        ConfigurableApplicationContext ctx = app.run(args);

        MonitoringApplication myApp = ctx.getBean(MonitoringApplication.class);
        myApp.testMonitoring();

        ctx.close();
    }

    private void testMonitoring() throws Exception {
        boolean running = true;

        while (running) {
            Collection<String> serviceNames = strategiesProvider.getFetchStrategy().getServiceNames();
            for (String serviceName : serviceNames) {
                String serviceAddress = strategiesProvider.getFetchStrategy().fetchServiceAddress(serviceName);
                String serviceStatus = getServiceStatus(serviceAddress);

                System.out.println(String.format("Service name: %s, address: %s, status: %s\n", serviceName, serviceAddress, serviceStatus));
            }

            Thread.sleep(5000);
        }
    }

    private String getServiceStatus(String serviceHealthcheckAddress) {
        HttpClient client = new DefaultHttpClient();
        HttpUriRequest request = new HttpHead(serviceHealthcheckAddress);

        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
                return "UP";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "DOWN";
    }

}
