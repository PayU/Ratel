package com.payu.discovery.client.config;

import com.payu.discovery.Discover;
import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.proxy.monitoring.ServiceDiscoveryHealth;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} to enable/disable Spring's
 * {@link com.payu.discovery.client.EnableServiceDiscovery} mechanism based on configuration properties.
 * <p/>
 * To disable auto export of annotation beans set <code>com.payu.discovery.enabled: false</code>.
 */
@Configuration
@ConditionalOnClass({Discover.class})
@ConditionalOnExpression("${com.payu.discovery.enabled:true}")
public class ServiceDiscoveryClientConfig implements BeanFactoryAware {

    private static final String DEFAULT_DISCOVERY_URL = "http://localhost:8090/server/discovery";

    private Environment environment;

    @Bean
    public DiscoveryClient discoveryClient() {
        final String property = environment.getProperty("serviceDiscovery.address", DEFAULT_DISCOVERY_URL);
        return new DiscoveryClient(property);
    }

    @Bean
    public RemoteAutowireCandidateResolver remoteAutowireCandidateResolver() {
        return new RemoteAutowireCandidateResolver(discoveryClient());
    }

    @Bean
    public AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer() {
        return new AutowireCandidateResolverConfigurer(remoteAutowireCandidateResolver());
    }

    @Bean
    public ServiceDiscoveryHealth serviceDiscoveryHealth(){
        return new ServiceDiscoveryHealth(discoveryClient());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        environment = beanFactory.getBean(Environment.class);
    }
}
