package com.payu.hackathon.discovery.scanner;

import static com.payu.hackathon.discovery.model.ServiceBuilder.aService;

import java.util.Objects;
import java.util.Set;

import javax.ws.rs.Path;

import org.reflections.Reflections;
import org.springframework.core.annotation.AnnotationUtils;

public class AnnotationScanner {
    private String packageToScan;

    private Reflections reflections;

    public AnnotationScanner(String packageToScan) {
        withPackage(packageToScan);
        reflections = buildReflection();

    }

    public void scan() {
        scanClasses().stream().
                forEach(it -> System.out.print(
                                aService()
                                  .withName(it.getName())
                                  .withPath(AnnotationUtils.findAnnotation(it, Path.class).value())

                                  .build()));


    }

    protected Set<Class<?>> scanClasses() {
        return reflections.getTypesAnnotatedWith(Path.class);
    }

    private void withPackage(String packageToScan) {
        this.packageToScan = packageToScan;
    }



    private Reflections buildReflection() {
        Objects.requireNonNull(packageToScan, "package must be not null");
        return new Reflections(packageToScan);

    }
}
