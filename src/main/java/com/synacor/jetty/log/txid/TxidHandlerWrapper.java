package com.synacor.jetty.log.txid;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;

public class TxidHandlerWrapper extends HandlerWrapper
{

	private TxidSource txidSource;

	public TxidHandlerWrapper()
		throws ClassNotFoundException, IllegalAccessException, InstantiationException 
	{   
		this(UuidSource.class);
	}

	public TxidHandlerWrapper(String s)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException 
	{   
		txidSource = TxidSource.getInstance(s);
	}

	public TxidHandlerWrapper(Class c)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException 
	{   
		txidSource = TxidSource.getInstance(c);
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
	{
		if (_handler!=null && isStarted())
		{
			if (request.getHeader("Syn-Txid") == null)
			{
				HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
				String txid = txidSource.getTxid();
				requestWrapper.addHeader("Syn-Txid", txid);
				baseRequest.setAttribute("com.synacor.jetty.log._headers.Syn-Txid", txid);

				_handler.handle(target,baseRequest, requestWrapper, response);
			}
			else
			{
				_handler.handle(target,baseRequest, request, response);
			}
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
