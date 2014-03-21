package com.payu.hackathon.discovery.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import com.payu.hackathon.discovery.model.Service;
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

    @Test
    public void shouldNotBuildServiceForClassWithoutAnnotation() {
        //given
        scanner = new AnnotationScanner("com.payu.hackathon.discovery.sampledomain.notscanned.service");
        //when
        List<? super Service> services = (List<? super Service>) scanner.scan().collect(Collectors.toList());
        //then
        assertThat(services).isEmpty();

    }

    @Test
    public void shouldBuildServiceWith3Methods() {
        //given
        scanner = new AnnotationScanner("com.payu.hackathon.discovery.sampledomain.service");
        //when
        List<Service> services = (List<Service>) scanner.scan().collect(Collectors.toList());
        //then
        assertThat(services).hasSize(1);
        assertThat(services.get(0).getMethods()).hasSize(3);



    }
}
