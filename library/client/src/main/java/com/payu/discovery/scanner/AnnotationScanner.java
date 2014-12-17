package com.payu.discovery.scanner;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;

public abstract class AnnotationScanner<K extends Annotation, T> {

	protected String packageToScan;
	protected Reflections reflections;
	protected Class<? extends Annotation> serviceAnnotationClass;
	protected Function<? super Class<?>, ? extends T> mapper;

	public AnnotationScanner(String packageToScan,
			Class<? extends Annotation> serviceAnnotationClass) {
		super();
		this.packageToScan = packageToScan;
		this.reflections = buildReflections();
		this.serviceAnnotationClass = serviceAnnotationClass;
		this.mapper = getMapper();
	}

	protected abstract Function<? super Class<?>, ? extends T> getMapper();

	private Reflections buildReflections() {
	
		return new Reflections(packageToScan, new TypeAnnotationsScanner(),
				new MethodAnnotationsScanner());
	
	}
	
	public Collection<? extends T> scan() {
        Set<Class<?>> classes = scanClasses(serviceAnnotationClass);
        return Collections2.transform(classes, mapper);
	}
	
	protected Set<Class<?>> scanClasses(
			Class<? extends Annotation> annotationClass) {
		return reflections.getTypesAnnotatedWith(annotationClass);
	}

}