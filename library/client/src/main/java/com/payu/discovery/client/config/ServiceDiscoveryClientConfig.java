package com.payu.discovery.client.config;

import com.payu.discovery.Discover;
import com.payu.discovery.proxy.ClientProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} to enable/disable Spring's
 * {@link com.payu.discovery.client.EnableServiceDiscovery} mechanism based on configuration properties.
 * <p/>
 * To disable auto export of annotation beans set <code>com.payu.discovery.enabled: false</code>.
 */
@Configuration
@ConditionalOnClass({Discover.class})
@ConditionalOnExpression("${com.payu.discovery.enabled:true}")
public class ServiceDiscoveryClientConfig {

    @Bean
    public RemoteAutowireCandidateResolver remoteAutowireCandidateResolver(ClientProducer clientProducer) {
        return new RemoteAutowireCandidateResolver(clientProducer);
    }

    @Bean
    public AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer(ClientProducer clientProducer) {
        return new AutowireCandidateResolverConfigurer(remoteAutowireCandidateResolver(clientProducer));
    }

}
