package com.payu.discovery.scanner;

import static com.payu.discovery.model.ServiceDescriptionBuilder.aService;

import java.lang.annotation.Annotation;

import com.google.common.base.Function;
import com.payu.discovery.model.ServiceDescriptor;

public class ServiceAnnotationScanner<K extends Annotation> extends AnnotationScanner<K, ServiceDescriptor> {

	private String appAddress;

	public ServiceAnnotationScanner(String packageToScan,
			Class<? extends Annotation> serviceAnnotationClass, String appAddress) {
		super(packageToScan, serviceAnnotationClass);
		this.appAddress = appAddress;
	}

	protected Function<? super Class<?>, ? extends ServiceDescriptor> getMapper() {
		return new Function<Class<?>, ServiceDescriptor>() {
            @Override
            public ServiceDescriptor apply(Class<?> it) {
                return aService().withName(it.getName())
                        .withAddress(ServiceAnnotationScanner.this.appAddress).build();
            }
        };
	}

}
