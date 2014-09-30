package com.payu.discovery.server.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.payu.discovery.model.ServiceBuilder;
import com.payu.discovery.server.RemoteRestDiscoveryServer;

@Component
public class ServiceRegisterPostProcessor implements BeanPostProcessor {
	
	
	@Autowired
    private RemoteRestDiscoveryServer server;
	
	//TODO - hardcoded address
	private String address = "http://localhost:8080/orderService";
	
	

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
    	if (isService(o)) {
    		registerService(o);
    		System.out.println("Bean '" + o + "' published as a service: " + o.toString());
    	}
        return o;
    }

	private void registerService(Object o) {
		com.payu.discovery.model.Service service = buildService(o);
		System.out.println("Registgering service " + service);
		server.registerService(service);
	}

	private com.payu.discovery.model.Service buildService(Object o) {
		String name = findServiceInterface(o).getCanonicalName();
		return ServiceBuilder.aService().withName(name).withAddress(address).build();
		
	}

	private Class<?> findServiceInterface(Object o) {
		Class<?>[] interfaces = o.getClass().getInterfaces();
		for (Class<?> interf : interfaces) {
			return interf;
		}
		return null;
	}

	private boolean isService(Object o) {
		return o.getClass().getAnnotation(Service.class) != null;
	}
}
