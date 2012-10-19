package com.craigrueda.webkeypad.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

public class DisableJSessionIdFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		if (!(req instanceof HttpServletRequest)) {
			chain.doFilter(req, resp);
			return;
		}
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		// clear session if session id in URL
		if (request.isRequestedSessionIdFromURL())
		{
			HttpSession session = request.getSession();
			if (session != null) session.invalidate();
		}
				
		// wrap response to remove URL encoding
		HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(response)
		{
			@Override
			public String encodeRedirectUrl(String url) { return url; }

			@Override
			public String encodeRedirectURL(String url) { return url; }

			@Override
			public String encodeUrl(String url) { return url; }

			@Override
			public String encodeURL(String url) { return url; }
		};
		
		chain.doFilter(request, wrappedResponse);
	}
	
	@Override
 	public void destroy() {	}

	@Override
	public void init(FilterConfig arg0) throws ServletException { }
}
