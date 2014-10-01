package com.payu.soa.example.client;

import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;

import com.payu.server.model.Order;
import com.payu.server.service.OrderService;

public class MyAutowireCandidateResolver extends
		ContextAnnotationAutowireCandidateResolver implements
		AutowireCandidateResolver {

	@Override
	protected Object buildLazyResolutionProxy(DependencyDescriptor descriptor,
			String beanName) {
		if (descriptor.getField().getType().isAssignableFrom(OrderService.class)) {
			return new OrderService() {
				
				@Override
				public Order getOrder(Long id) {
					System.out.println(" ********* Call to getOrder");
					return new Order();
				}
				
				@Override
				public void createOrder(Order order) {
					System.out.println(" ********  Call to createOrder");
					
				}
			};
		}
		return super.buildLazyResolutionProxy(descriptor, beanName);
	}

}
