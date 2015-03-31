package com.payu.ratel.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory;
import com.payu.ratel.context.ProcessContext;
import com.payu.ratel.register.ServiceRegisterPostProcessor;

public class TracingFilter implements Filter {

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

	private void doFilterHttp(HttpServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String processIdHeader = request.getHeader(ProcessContext.RATEL_HEADER_PROCESS_ID);
		if (processIdHeader != null) {
			filterWithProcessTracing(request, response, chain, processIdHeader);
		} else {
			if (isRatelRequest(request)) {
				ProcessContext.getInstance().generateProcessIdentifier();
			}
			chain.doFilter(request, response);
		}
	}

	private boolean isRatelRequest(HttpServletRequest request) {
		return request.getRequestURI().contains(ServiceRegisterPostProcessorFactory.RATEL_PATH);
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
