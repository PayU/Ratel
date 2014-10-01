package com.payu.soa.example.client;

import com.payu.discovery.proxy.SpringRemoteBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.payu.soa")
@Configuration
@EnableAutoConfiguration
public class ClientTestApp extends SpringBootServletInitializer {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = new MySpringApplication(new Object[] { ClientTestApp.class, SoaFramework.class }).run(args);
	}

    @Bean
    public SpringRemoteBeanFactory springRemoteBeanFactory() {
        return new SpringRemoteBeanFactory();
    }

    @Bean
    public TestBean testBean() {
        return new TestBean();
    }


    @Bean
    public MyAutowireCandidateResolver myAutowireCandidateResolver(){
        return new MyAutowireCandidateResolver();
    }

    @Bean
    public AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer(){
        AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer = new AutowireCandidateResolverConfigurer();
        autowireCandidateResolverConfigurer.setAutowireCandidateResolver(myAutowireCandidateResolver());
        return autowireCandidateResolverConfigurer;

    }

}
