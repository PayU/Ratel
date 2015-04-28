package com.payu.ratel.context.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.payu.ratel.context.ProcessContext;

/**
 * A filter applied on all requests sent to ratel requests. It is responsible
 * for copying {@link ProcessContext} data from http headers to
 * {@link ProcessContext#getInstance()} If the processId is not present in the
 * request headers, a new one will be picked at random.
 *
 * This filter should be mapped on all ratel services. Please see also
 * {@link com.payu.ratel.register.RatelHessianServiceExporter}.
 * */
public class TracingFilter implements Filter {

  public static final String RATEL_HEADER_PROCESS_ID = "ratel-process-id";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    if (request instanceof HttpServletRequest) {
      HttpServletRequest req = (HttpServletRequest) request;
      doFilterHttp(req, response, chain);
    } else {
      chain.doFilter(request, response);
    }

  }

  private void doFilterHttp(HttpServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String processIdHeader = request.getHeader(TracingFilter.RATEL_HEADER_PROCESS_ID);
    if (processIdHeader != null) {
      filterWithProcessTracing(request, response, chain, processIdHeader);
    } else {
      ProcessContext.getInstance().generateProcessIdentifier();
      chain.doFilter(request, response);
    }
  }

  private void filterWithProcessTracing(ServletRequest request, ServletResponse response, FilterChain chain,
      String processIdHeader) throws IOException, ServletException {
    assert ProcessContext.getInstance().getProcessIdentifier() == null;
    ProcessContext.getInstance().setProcessIdentifier(processIdHeader);
    try {
      chain.doFilter(request, response);
    } finally {
      ProcessContext.getInstance().clearProcessIdentifier();
    }
  }

  @Override
  public void destroy() {

  }

}
