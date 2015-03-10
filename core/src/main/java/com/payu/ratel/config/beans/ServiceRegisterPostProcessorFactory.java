package com.payu.ratel.config.beans;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;

import com.payu.ratel.register.RegisterStrategy;
import com.payu.ratel.register.ServiceRegisterPostProcessor;

public class ServiceRegisterPostProcessorFactory {

    public static final String JBOSS_BIND_ADDRESS = "jboss.bind.address";
    public static final String JBOSS_BIND_PORT = "jboss.bind.port";
    public static final String RATEL_PATH = "/ratelServices/";

    public ServiceRegisterPostProcessor create(ConfigurableListableBeanFactory beanFactory, RegisterStrategy registerStrategy) {

        final Environment environment = beanFactory.getBean(Environment.class);

        ServletContext servletContext = null;
        try {
            servletContext = beanFactory.getBean(ServletContext.class);
        } catch (BeansException e) {
        }

        final String host = environment.getProperty(JBOSS_BIND_ADDRESS, "localhost");
        final String port = environment.getProperty(JBOSS_BIND_PORT, "8080");
        final String contextRoot = servletContext != null ? servletContext.getContextPath() : "";

        final String address = String.format("http://%s:%s%s%s", host, port, contextRoot, RATEL_PATH);

        return new ServiceRegisterPostProcessor(beanFactory, registerStrategy, address);
    }
}
