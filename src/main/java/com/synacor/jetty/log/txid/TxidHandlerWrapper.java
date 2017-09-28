/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.jetty.log.txid;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;

import com.synacor.txid.TxidFactory;
import com.synacor.txid.Txid120Factory;

public class TxidHandlerWrapper
	extends HandlerWrapper
{

	private String headerName;
	private TxidFactory txidFactory;

	public TxidHandlerWrapper()
		throws ClassNotFoundException, IllegalAccessException, InstantiationException 
	{   
		this("X-Transaction-ID", Txid120Factory.class);
	}

	public TxidHandlerWrapper(String headerName)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException 
	{
		this(headerName, Txid120Factory.class);
	}

	public TxidHandlerWrapper(Class txidClass)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException 
	{
		this("X-Transaction-ID", txidClass);
	}

	public TxidHandlerWrapper(String headerName, String txidClassName)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException 
	{   
		this.headerName = headerName;
		txidFactory = TxidFactory.getInstance(txidClassName);
	}

	public TxidHandlerWrapper(String headerName, Class txidClass)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException 
	{   
		this.headerName = headerName;
		txidFactory = TxidFactory.getInstance(txidClass);
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException
	{
		if (_handler != null && isStarted())
		{
			if (request.getHeader(headerName) == null)
			{
				HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
				String txid = txidFactory.getTxid();
				requestWrapper.addHeader(headerName, txid);
				baseRequest.setAttribute("com.synacor.jetty.log.headers." + headerName, txid);

				_handler.handle(target, baseRequest, requestWrapper, response);
			}
			else
			{
				_handler.handle(target, baseRequest, request, response);
			}
		}
	}

}
