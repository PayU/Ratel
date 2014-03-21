package com.payu.hachaton.scanner;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.Objects;

public class AnnotationScanner {
    private String packageToScan;

    private Reflections reflections;

    public void withPackage(String packageToScan) {
        this.packageToScan = packageToScan;
    }

    public Reflections build() {
        Objects.requireNonNull(packageToScan, "package must be not null");
        return new Reflections(
                    new ConfigurationBuilder().filterInputsBy(new FilterBuilder().includePackage(packageToScan)
                ).build());

    }

}
