package com.payu.discovery.scanner;

import static com.payu.discovery.model.ServiceBuilder.aService;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import com.payu.discovery.model.ServiceDescriptor;

public class ServiceAnnotationScanner<K extends Annotation> extends AnnotationScanner<K, ServiceDescriptor> {

	private String appAddress;

	public ServiceAnnotationScanner(String packageToScan,
			Class<? extends Annotation> serviceAnnotationClass, String appAddress) {
		super(packageToScan, serviceAnnotationClass);
		this.appAddress = appAddress;
	}

	protected Function<? super Class<?>, ? extends ServiceDescriptor> getMapper() {
		return it -> aService().withName(it.getName())
				.withAddress(this.appAddress).build();
	}

}
