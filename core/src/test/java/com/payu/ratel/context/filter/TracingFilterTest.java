package com.payu.ratel.context.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.payu.ratel.context.ProcessContext;

public class TracingFilterTest {

  private TracingFilter filter = new TracingFilter();

  private ServletRequest request;

  private ServletResponse response;

  private HttpServletRequest httpRequest;

  private HttpServletResponse httpResponse;

  private FilterChain chainMock;

  String processIdInsideFilter;
  private FilterChain chainStub = new FilterChain() {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
      processIdInsideFilter = ProcessContext.getInstance().getProcessIdentifier();
    }

  };

  @Before
  public void before() {
    request = mock(ServletRequest.class);
    response = mock(ServletResponse.class);
    httpRequest = mock(HttpServletRequest.class);
    httpResponse = mock(HttpServletResponse.class);
    chainMock = mock(FilterChain.class);
    processIdInsideFilter = null;
  }

  @Test
  public void shouldFilterNonHttpRequests() throws Exception {

    // given

    // when
    filter.doFilter(request, response, chainMock);

    // then
    verify(chainMock).doFilter(eq(request), eq(response));

  }

  @Test
  public void shouldRewriteProcessIdFromHeaderToProcessContext() throws Exception {

    // given
    when(httpRequest.getHeader(eq(TracingFilter.RATEL_HEADER_PROCESS_ID))).thenReturn("123");

    assertThat(ProcessContext.getInstance().getProcessIdentifier()).isNull();

    // when
    filter.doFilter(httpRequest, httpResponse, chainStub);

    // then
    assertThat(ProcessContext.getInstance().getProcessIdentifier()).isNull();
    assertThat(processIdInsideFilter).isEqualTo("123");

  }

  @Test
  public void shouldGenerateProcessIdWhenNotSetInHeader() throws Exception {

    // given
    assertThat(ProcessContext.getInstance().getProcessIdentifier()).isNull();

    // when
    filter.doFilter(httpRequest, httpResponse, chainStub);

    // then
    assertThat(ProcessContext.getInstance().getProcessIdentifier()).isNull();
    assertThat(processIdInsideFilter).isNotNull();

  }
  
  @Test
  public void shouldClearProcessIdAfterExceptionInFilter() throws Exception {
    
    // given
    assertThat(ProcessContext.getInstance().getProcessIdentifier()).isNull();
    Mockito.doThrow(NumberFormatException.class).when(chainMock).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    
    try {
      // when
      filter.doFilter(httpRequest, httpResponse, chainMock);
    } catch (NumberFormatException e) {
    }
    
    // then
    assertThat(ProcessContext.getInstance().getProcessIdentifier()).isNull();
    
  }
}
