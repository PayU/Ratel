package com.payu.discovery.register.config;

import com.payu.discovery.config.ServerDiscoveryConfig;
import com.payu.discovery.config.ZookeeperDiscoveryConfig;
import com.payu.discovery.register.RegisterStrategy;
import com.payu.discovery.register.ServiceRegisterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.servlet.ServletContext;

import static com.payu.discovery.config.RatelContextInitializer.SERVICE_DISCOVERY_ENABLED;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} to enable/disable Spring's
 * <p/>
 * To disable auto export of annotation beans set <code>serviceDiscovery.enabled: false</code>.
 */
@Configuration
@Profile(SERVICE_DISCOVERY_ENABLED)
@Import({ZookeeperDiscoveryConfig.class, ServerDiscoveryConfig.class})
public class DiscoveryServiceConfig {

    public static final String JBOSS_BIND_ADDRESS = "jboss.bind.address";
    public static final String JBOSS_BIND_PORT = "jboss.bind.port";
    public static final String RATEL_PATH = "/ratelServices/";

    @Autowired
    private Environment environment;

    @Autowired(required = false)
    private ServletContext servletContext;

    @Bean
    public ServiceRegisterPostProcessor serviceRegisterPostProcessor(ConfigurableListableBeanFactory configurableListableBeanFactory,
                                                                     RegisterStrategy registerStrategy) {

        final String host = environment.getProperty(JBOSS_BIND_ADDRESS, "localhost");
        final String port = environment.getProperty(JBOSS_BIND_PORT, "8080");
        final String contextRoot = servletContext != null ? servletContext.getContextPath() : "";

        final String address = String.format("http://%s:%s%s%s", host, port, contextRoot, RATEL_PATH);

        return new ServiceRegisterPostProcessor(configurableListableBeanFactory, registerStrategy, address);
    }

}
