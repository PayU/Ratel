package com.payu.discovery.config;

import static com.payu.discovery.config.RatelContextInitializer.SERVICE_DISCOVERY_ENABLED;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import com.payu.discovery.client.AutowireCandidateResolverConfigurer;
import com.payu.discovery.client.ClientProxyGenerator;
import com.payu.discovery.client.FetchStrategy;
import com.payu.discovery.client.RemoteAutowireCandidateResolver;
import com.payu.discovery.register.RegisterStrategy;
import com.payu.discovery.register.ServiceRegisterPostProcessor;


/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} to enable/disable Spring's
 * {@link com.payu.discovery.client.EnableServiceDiscovery} mechanism based on configuration properties.
 * <p/>
 * To disable auto export of annotation beans set <code>serviceDiscovery.enabled: false</code>.
 */
@Configuration
@Profile(SERVICE_DISCOVERY_ENABLED)
@Import({ZookeeperDiscoveryConfig.class, InMemoryDiscoveryConfig.class})
public class ServiceDiscoveryConfig implements BeanFactoryAware {

    public static final String JBOSS_BIND_ADDRESS = "jboss.bind.address";
    public static final String JBOSS_BIND_PORT = "jboss.bind.port";
    public static final String RATEL_PATH = "/ratelServices/";

    private Environment environment;

    private ServletContext servletContext;

    @Bean
    public ServiceRegisterPostProcessor serviceRegisterPostProcessor(ConfigurableListableBeanFactory configurableListableBeanFactory,
                                                                     RegisterStrategy registerStrategy) {

        final String host = environment.getProperty(ServiceDiscoveryConfig.JBOSS_BIND_ADDRESS, "localhost");
        final String port = environment.getProperty(ServiceDiscoveryConfig.JBOSS_BIND_PORT, "8080");
        final String contextRoot = servletContext != null ? servletContext.getContextPath() : "";

        final String address = String.format("http://%s:%s%s%s", host, port, contextRoot, ServiceDiscoveryConfig.RATEL_PATH);

        return new ServiceRegisterPostProcessor(configurableListableBeanFactory, registerStrategy, address);
    }

    @Bean
    public AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer(FetchStrategy fetchStrategy,
                                                                                   ClientProxyGenerator clientProxyGenerator) {

        return new AutowireCandidateResolverConfigurer(new RemoteAutowireCandidateResolver(fetchStrategy, clientProxyGenerator));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        environment = beanFactory.getBean(Environment.class);

        try {
            servletContext = beanFactory.getBean(ServletContext.class);
        } catch (BeansException e) {
        }
    }

}
