package com.payu.ratel.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.payu.ratel.Discover;
import com.payu.ratel.context.ProcessContext;
import com.payu.ratel.tests.service.tracing.ProcessIdPassingService;
import com.payu.ratel.tests.service.tracing.ProcessIdTargetService;
import com.payu.ratel.tests.service.tracing.TracingTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@RatelTest(registerServices = TracingTestConfiguration.class)
public class ProcessTracingTest {


    @Discover
    private ProcessIdPassingService passingService;

    @Discover
    private ProcessIdTargetService targetService;


    @Test
    public void shouldGenerateProcessIdWhenNotSetInThread() throws Exception {
        //given
        assertThat((targetService.getProcessId())).isNull();

        //when
        passingService.passProcessId();

        //then
        assertThat((targetService.getProcessId())).isNotNull();
    }

    @Test
    public void shouldGenerateNewProcessIdWithEveryCall() throws Exception {
        //given
        assertThat((targetService.getProcessId())).isNull();
        ProcessContext.getInstance().clearProcessIdentifier();

        //when
        passingService.passProcessId();
        String processId1 = targetService.getProcessId();
        ProcessContext.getInstance().clearProcessIdentifier();

        passingService.passProcessId();
        String processId2 = targetService.getProcessId();

        //then
        assertThat(processId1).isNotEqualTo(processId2);
    }


    @Test
    public void shouldPassProcessIdThroughNetworkCall() throws Exception {

        //given
        assertThat((targetService.getProcessId())).isNull();

        //when
        ProcessContext.getInstance().setProcessIdentifier("123");
        passingService.passProcessId();

        //then
        assertThat((targetService.getProcessId())).isEqualTo("123");
        assertThat((passingService.getProcessId())).isEqualTo(targetService.getProcessId());
    }

}
