package com.payu.ratel.register;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.remoting.caucho.HessianServiceExporter;

import com.payu.ratel.filter.TracingFilter;

public class RatelHessianServiceExporter extends HessianServiceExporter {
	
	private TracingFilter tracingFilter = new TracingFilter();

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		
		//This ugly trick makes it backward-compatibile with older servlet-api versions. 
		//TODO: Subject to refactoring
		
		tracingFilter.doFilter(request, response, new FilterChain() {
			
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
				RatelHessianServiceExporter.super.handleRequest((HttpServletRequest)request, (HttpServletResponse)response);
			}
		});
	}
}
