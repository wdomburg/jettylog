package com.synacor.jetty.log.txid;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.synacor.txid.TxidFactory;
import com.synacor.txid.Txid120Factory;

public class TxidFilter implements Filter
{

	private final String headerName;
	private final TxidFactory txidFactory;

	public TxidFilter()
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		this("X-Transaction-ID", Txid120Factory.class);
	}

	public TxidFilter(String headerName)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		this(headerName, Txid120Factory.class);
	}

	public TxidFilter(Class txidClass)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		this("X-Transaction-ID", txidClass);
	}

	public TxidFilter(String headerName, String txidClassName)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		this.headerName = headerName;
		txidFactory = TxidFactory.getInstance(txidClassName);
	}

	public TxidFilter(String headerName, Class txidClass)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		this.headerName = headerName;
		txidFactory = TxidFactory.getInstance(txidClass);
	}

	@Override
	public void init(FilterConfig filterConfig)
		throws ServletException
	{
	}

	@Override
	public void destroy()
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String txid = httpRequest.getHeader(headerName);
		
		if (txid == null)
		{
			txid = txidFactory.getTxid();

			HeaderMapRequestWrapper wrappedRequest = new HeaderMapRequestWrapper(httpRequest);
			wrappedRequest.addHeader(headerName, txid);
			chain.doFilter(wrappedRequest, httpResponse);
		}
		else
		{
			chain.doFilter(httpRequest, httpResponse);
		}
	}

}
