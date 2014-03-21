package com.payu.hackaton.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.payu.hackathon.discovery.scanner.AnnotationScanner;

public class AnnotationScannerTest {

    AnnotationScanner scanner;

    @Test
    @Ignore
    public void shouldScanClassesInProperPackage() {
        //given
        scanner = new AnnotationScanner("com.payu.hackaton.sampledomain.service");
        //when
        Set<Class<?>> classes = scanner.scanClasses();
        //then
        assertThat(classes).hasSize(1);
        assertThat(classes).contains(com.payu.hackaton.sampledomain.service.RestServiceImpl.class);
    }

    @Test
    @Ignore
    public void shouldNoScanClassesNotAnnotatedPath() {
        //given
        scanner = new AnnotationScanner("com.payu.hackaton.sampledomain.notscanned.service");
        //when
        Set<Class<?>> classes = scanner.scanClasses();
        //then
        assertThat(classes).isEmpty();
    }
}
