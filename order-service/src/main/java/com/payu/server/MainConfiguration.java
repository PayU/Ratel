package com.payu.server;


import com.payu.discovery.server.ServiceRegister;
import com.payu.discovery.server.config.DiscoveryServiceConfig;
import com.payu.server.model.OrderDatabase;
import com.payu.server.service.OrderService;
import com.payu.server.service.OrderServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;

@ComponentScan(basePackages = "com.payu.server")
@Configuration
@EnableAutoConfiguration
public class MainConfiguration extends SpringBootServletInitializer {

    private static final String appAddress = "http://localhost:8080/";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Object[]{
                MainConfiguration.class, DiscoveryServiceConfig.class }, args);
        ServiceRegister serviceRegister = new ServiceRegister("com.payu", appAddress);
        serviceRegister.registerServices();
    }

    @Bean
    public OrderDatabase orderDatabase() {
        return new OrderDatabase();
    }

    @Bean
    public OrderServiceImpl orderService() {
        return new OrderServiceImpl();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainConfiguration.class);
    }

    @Bean(name = "/orderService")
    public HessianServiceExporter hessianServiceExporter() {
        HessianServiceExporter hessianServiceExporter = new HessianServiceExporter();
        hessianServiceExporter.setService(orderService());
        hessianServiceExporter.setServiceInterface(OrderService.class);
        return hessianServiceExporter;
    }

}
