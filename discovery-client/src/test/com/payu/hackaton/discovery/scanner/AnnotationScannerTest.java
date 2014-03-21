package com.payu.hackaton.discovery.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Test;

import com.payu.hackathon.discovery.scanner.AnnotationScanner;

public class AnnotationScannerTest {

    AnnotationScanner scanner;

    @Test
    public void shouldScanClassesInProperPackage() {
        //given
        scanner = new AnnotationScanner("com.payu.hackaton.discovery.sampledomain.service");
        //when
        Set<Class<?>> classes = scanner.scanClasses();
        //then
        assertThat(classes).hasSize(1);
        assertThat(classes).contains(com.payu.hackaton.discovery.sampledomain.service.RestServiceImpl.class);
    }

    @Test
    public void shouldNoScanClassesNotAnnotatedPath() {
        //given
        scanner = new AnnotationScanner("com.payu.hackaton.discovery.sampledomain.notscanned.service");
        //when
        Set<Class<?>> classes = scanner.scanClasses();
        //then
        assertThat(classes).isEmpty();
    }
}
