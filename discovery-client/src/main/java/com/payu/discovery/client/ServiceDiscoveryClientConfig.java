package com.payu.discovery.client;

import com.payu.discovery.RemoteService;
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
@ConditionalOnClass({RemoteService.class})
@ConditionalOnExpression("${com.payu.discovery.enabled:true}")
public class ServiceDiscoveryClientConfig implements BeanFactoryAware {

    private Environment environment;

    public static final String DEFAULT_DISCOVERY_URL = "http://localhost:8090/server/discovery";

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

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        environment = beanFactory.getBean(Environment.class);
    }
}
