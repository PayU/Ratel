package com.payu.discovery.register.config;

import com.payu.discovery.Publish;
import com.payu.discovery.config.ServerDiscoveryConfig;
import com.payu.discovery.config.ZookeeperDiscoveryConfig;
import com.payu.discovery.register.RegisterStrategy;
import com.payu.discovery.register.ServiceRegisterPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} to enable/disable Spring's
 * <p/>
 * To disable auto export of annotation beans set <code>com.payu.discovery.enabled: false</code>.
 */
@Configuration
@ConditionalOnClass({Publish.class})
@ConditionalOnExpression("${com.payu.discovery.enabled:true}")
@Import({ZookeeperDiscoveryConfig.class, ServerDiscoveryConfig.class})
public class DiscoveryServiceConfig {

    @Bean
    public ServiceRegisterPostProcessor serviceRegisterPostProcessor(RegisterStrategy registerStrategy) {
        return new ServiceRegisterPostProcessor(registerStrategy);
    }

}
