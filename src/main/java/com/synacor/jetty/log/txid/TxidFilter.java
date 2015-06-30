package com.synacor.jetty.log.txid;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class TxidFilter implements Filter
{

	private final TxidSource txidSource;

	public TxidFilter()
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		this(UuidSource.class);
	}

	public TxidFilter(String s)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		txidSource = TxidSource.getInstance(s);
	}

	public TxidFilter(Class c)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		txidSource = TxidSource.getInstance(c);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String txid = httpRequest.getHeader("Syn-Txid");
		
		if (txid == null)
		{
			txid = txidSource.getTxid();
			request.setAttribute("Syn-Txid", txid);

			HeaderMapRequestWrapper wrappedRequest = new HeaderMapRequestWrapper(httpRequest);
			wrappedRequest.addHeader("Syn-Txid", txid);
			chain.doFilter(wrappedRequest, httpResponse);
		}
		else
		{
			request.setAttribute("Syn-Txid", txid);
			chain.doFilter(httpRequest, httpResponse);
		}
	}

	// http://stackoverflow.com/questions/2811769/adding-an-http-header-to-the-request-in-a-servlet-filter
	// http://sandeepmore.com/blog/2010/06/12/modifying-http-headers-using-java/
	// http://bijubnair.blogspot.de/2008/12/adding-header-information-to-existing.html
	public class HeaderMapRequestWrapper extends HttpServletRequestWrapper {

		public HeaderMapRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		private Map<String, String> headerMap = new HashMap<String, String>();

		public void addHeader(String name, String value) {
			headerMap.put(name, value);
		}

		@Override
		public String getHeader(String name) {
			String headerValue = super.getHeader(name);
			if (headerMap.containsKey(name)) {
				headerValue = headerMap.get(name);
			}
			return headerValue;
		}

		@Override
		public Enumeration<String> getHeaderNames() {
			List<String> names = Collections.list(super.getHeaderNames());
			for (String name : headerMap.keySet()) {
				names.add(name);
			}
			return Collections.enumeration(names);
		}

		@Override
		public Enumeration<String> getHeaders(String name) {
			List<String> values = Collections.list(super.getHeaders(name));
			if (headerMap.containsKey(name)) {
				values.add(headerMap.get(name));
			}
			return Collections.enumeration(values);
		}
	}
}
