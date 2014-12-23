package com.payu.discovery.register.config;

import com.payu.discovery.Publish;
import com.payu.discovery.config.ServerDiscoveryConfig;
import com.payu.discovery.config.ZookeeperDiscoveryConfig;
import com.payu.discovery.register.RegisterStrategy;
import com.payu.discovery.register.ServiceRegisterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

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

    public static final String JBOSS_BIND_ADDRESS = "jboss.bind.address";
    public static final String JBOSS_BIND_PORT = "jboss.bind.port";

    @Autowired
    private Environment environment;

    @Bean
    public ServiceRegisterPostProcessor serviceRegisterPostProcessor(ConfigurableListableBeanFactory configurableListableBeanFactory,
                                                                     RegisterStrategy registerStrategy) {

        final String host = environment.getProperty(JBOSS_BIND_ADDRESS, "localhost");
        final String port = environment.getProperty(JBOSS_BIND_PORT, "8080");
        String address = String.format("http://%s:%s/", host, port);

        return new ServiceRegisterPostProcessor(configurableListableBeanFactory, registerStrategy, address);
    }

}
