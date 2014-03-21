package com.payu.hackathon.front;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import com.payu.hackathon.discovery.client.DiscoveryClient;
import com.payu.hackathon.discovery.model.Service;

@RunWith(MockitoJUnitRunner.class)
public class DiscoveryQueryControllerTest {

    MockMvc mockMvc;

    @InjectMocks
    DiscoveryQueryController controller;

    @Mock
    DiscoveryClient discoveryClient;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
        given(discoveryClient.fetchAllServices()).willReturn(
                asList(new Service("foo", Collections.<Service.Method>emptyList(), "http://localhost", "order"))
        );
    }

    @Test
    public void shouldOpenIndex() throws Exception {
        this.mockMvc.perform(
                get("/services")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[*].rel").value("self"))
                .andExpect(jsonPath("$.services[*].name").value("foo"));
    }
}
