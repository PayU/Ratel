package com.payu.discovery.proxy;

import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.model.ServiceDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpringRemoteBeanFactory extends InstantiationAwareBeanPostProcessorAdapter implements BeanFactoryAware {

    private AutowireCapableBeanFactory beanFactory;

    private final HessianClientProducer hessianClientProducer;

    private final DiscoveryClient discoveryClient = new DiscoveryClient();

    public SpringRemoteBeanFactory() {
        final Map<String, ServiceDescriptor> services = discoveryClient
                .fetchAllServices()
                .stream()
                .collect(Collectors.toMap(ServiceDescriptor::getName, Function.<ServiceDescriptor>identity()));

        hessianClientProducer = new HessianClientProducer(services);
    }

    @Override
    public Object postProcessBeforeInstantiation(Class beanClass, String beanName) throws BeansException {
        if (beanClass.isAnnotationPresent(RemoteService.class)) {
            return hessianClientProducer.produce(beanClass);
        }
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (AutowireCapableBeanFactory) beanFactory;

    }

}