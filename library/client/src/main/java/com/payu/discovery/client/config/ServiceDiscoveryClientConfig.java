package com.payu.discovery.client.config;

import com.payu.discovery.Discover;
import com.payu.discovery.client.AutowireCandidateResolverConfigurer;
import com.payu.discovery.client.ClientProxyGenerator;
import com.payu.discovery.client.FetchStrategy;
import com.payu.discovery.client.RemoteAutowireCandidateResolver;
import com.payu.discovery.config.ServerDiscoveryConfig;
import com.payu.discovery.config.ZookeeperDiscoveryConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} to enable/disable Spring's
 * {@link com.payu.discovery.client.EnableServiceDiscovery} mechanism based on configuration properties.
 * <p/>
 * To disable auto export of annotation beans set <code>serviceDiscovery.enabled: false</code>.
 */
@Configuration
@ConditionalOnClass({Discover.class})
@ConditionalOnExpression("${serviceDiscovery.enabled:true}")
@Import({ZookeeperDiscoveryConfig.class, ServerDiscoveryConfig.class})
public class ServiceDiscoveryClientConfig {

    @Bean
    public AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer(FetchStrategy fetchStrategy,
                                                                                   ClientProxyGenerator clientProxyGenerator) {

        return new AutowireCandidateResolverConfigurer(new RemoteAutowireCandidateResolver(fetchStrategy, clientProxyGenerator));
    }

}
