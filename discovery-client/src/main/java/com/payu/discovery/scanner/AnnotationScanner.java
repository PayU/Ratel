package com.payu.discovery.scanner;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

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
	
	public Stream<T> scan() {
		return scanClasses(serviceAnnotationClass).stream().map(mapper);
	}
	
	protected Set<Class<?>> scanClasses(
			Class<? extends Annotation> annotationClass) {
		return reflections.getTypesAnnotatedWith(annotationClass);
	}

}