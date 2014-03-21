package com.payu.hackathon.discovery.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Test;

import com.payu.hackathon.discovery.sampledomain.service.RestServiceImpl;

public class AnnotationScannerTest {

    AnnotationScanner scanner;

    @Test
    public void shouldScanClassesInProperPackage() {
        //given
        scanner = new AnnotationScanner("com.payu.hackathon.discovery.sampledomain.service");
        //when
        Set<Class<?>> classes = scanner.scanClasses();
        //then
        assertThat(classes).hasSize(1);
        assertThat(classes).contains(RestServiceImpl.class);
    }

    @Test
    public void shouldNoScanClassesNotAnnotatedPath() {
        //given
        scanner = new AnnotationScanner("com.payu.hackathon.discovery.sampledomain.notscanned.service");
        //when
        Set<Class<?>> classes = scanner.scanClasses();
        //then
        assertThat(classes).isEmpty();
    }
}
