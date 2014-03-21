package com.payu.hackathon.discovery.scanner;

import java.util.Objects;
import java.util.Set;

import javax.ws.rs.Path;

import org.reflections.Reflections;
public class AnnotationScanner {
    private String packageToScan;

    private Reflections reflections;

    public AnnotationScanner(String packageToScan) {
        withPackage(packageToScan);
        reflections = buildReflection();

    }

    private void withPackage(String packageToScan) {
        this.packageToScan = packageToScan;
    }

    private Reflections buildReflection() {
        Objects.requireNonNull(packageToScan, "package must be not null");
        return new Reflections(packageToScan);

    }

    public Set<Class<?>> scanClasses() {
        return reflections.getTypesAnnotatedWith(Path.class);
    }
}
