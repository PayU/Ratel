package com.payu.order.server;


import com.payu.discovery.server.config.DiscoveryServiceConfig;
import com.payu.order.server.model.OrderDatabase;
import com.payu.order.server.service.OrderService;
import com.payu.order.server.service.OrderServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.remoting.caucho.HessianServiceExporter;

@ComponentScan(basePackages = "com.payu.order.server")
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:propertasy.properties")
@Import(DiscoveryServiceConfig.class)
public class MainConfiguration {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainConfiguration.class, args);
    }

    @Bean
    public OrderDatabase orderDatabase() {
        return new OrderDatabase();
    }

    @Bean
    public OrderServiceImpl orderService() {
        return new OrderServiceImpl();
    }

    @Bean(name = "/orderService")
    public HessianServiceExporter hessianServiceExporter() {
        HessianServiceExporter hessianServiceExporter = new HessianServiceExporter();
        hessianServiceExporter.setService(orderService());
        hessianServiceExporter.setServiceInterface(OrderService.class);
        return hessianServiceExporter;
    }

}
