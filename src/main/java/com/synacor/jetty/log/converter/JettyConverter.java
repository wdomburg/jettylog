/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.jetty.log.converter;

import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;

import com.synacor.jetty.log.Event;
import com.synacor.jetty.log.JettyEvent;

public abstract class JettyConverter extends Converter
{   

	public static class Attribute extends JettyConverter
	{
		private final String name;

		public Attribute(String name)
		{
			this.name = name;
		}

		public String format(StringBuilder entry, JettyEvent event)
		{
			String value = (String) event.request.getAttribute(name);
			entry.append(value != null ? value : "-");
			return child.format(entry, event);
		}
	}

	public static class BytesWritten extends JettyConverter
	{

		private final char empty;

		public BytesWritten()
		{
			this(false);
		}

		public BytesWritten(boolean clf)
		{
			this.empty = clf ? '-' : '0';
		}
	
		public String format(StringBuilder entry, JettyEvent event)
		{   
			// NOTE: This changes to .getHttpChannel().getBytesWritten() for Jetty 9.x
			long bytes = event.response.getContentCount();
			entry.append(bytes > 0 ? bytes : empty);
			return child.format(entry, event);
		}
	}

	public static class Header extends JettyConverter
	{
		private final String headerName;

		public Header(String headerName)
		{
			this.headerName = headerName;
		}

		public String format(StringBuilder entry, JettyEvent event)
		{
			String value = event.request.getHeader(headerName);
			if (value == null)
			{
				value = Objects.toString(event.request.getAttribute("com.synacor.jetty.log.headers." + headerName), null);
			}
			entry.append(value != null ? value : "");
			return child.format(entry, event);
		}
	}

	public static class Latency extends JettyConverter
	{
		public String format(StringBuilder entry, JettyEvent event)
		{   
			long now = System.currentTimeMillis();
			entry.append(now - event.request.getTimeStamp());
			return child.format(entry, event);
		}
	}

	public static class LatencySeconds extends JettyConverter
	{
		public String format(StringBuilder entry, JettyEvent event)
		{
			long now = System.currentTimeMillis();
			entry.append(now - event.request.getTimeStamp() / 1000);
			return child.format(entry, event);
		}
	}

	public static class Method extends JettyConverter
	{
		public String format(StringBuilder entry, JettyEvent event)
		{   
			entry.append(event.request.getMethod());
			return child.format(entry, event);
		}
	}

	public static class Protocol extends JettyConverter
	{
		public String format(StringBuilder entry, JettyEvent event)
		{   
			entry.append(event.request.getProtocol());
			return child.format(entry, event);
		}
	}

	public static class RemoteAddress extends JettyConverter
	{
		public String format(StringBuilder entry, JettyEvent event)
		{   
			entry.append(event.request.getRemoteAddr());
			return child.format(entry, event);
		}
	}

	public static class RequestString extends JettyConverter
	{
		public String format(StringBuilder entry, JettyEvent event)
		{   
			entry.append(event.request.getMethod());
			entry.append(" ");
			entry.append(event.request.getRequestURI().toString());
			entry.append(" ");
			entry.append(event.request.getProtocol());
			return child.format(entry, event);
		}
	}

	public static class RequestUri extends JettyConverter
	{
		public String format(StringBuilder entry, JettyEvent event)
		{   
			entry.append(event.request.getRequestURI().toString());
			return child.format(entry, event);
		}
	}

	public static class RequestUrl extends JettyConverter
	{
		public String format(StringBuilder entry, JettyEvent event)
		{   
			entry.append(event.request.getRequestURL().toString());
			return child.format(entry, event);
		}
	}

	public static class Status extends JettyConverter
	{
		public String format(StringBuilder entry, JettyEvent event)
		{   
			entry.append(event.response.getStatus());
			return child.format(entry, event);
		}
	}

	public static class TimeReceived extends JettyConverter
	{
		private static final DateFormat dateFormat = new SimpleDateFormat("dd/MMM/YYYY:HH:mm:ss Z");

		public String format(StringBuilder entry, JettyEvent event)
		{   
			entry.append("[");
			entry.append(dateFormat.format(event.request.getTimeStamp()));
			entry.append("]");
			return child.format(entry, event);
		}
	}

	public static class Username extends JettyConverter
	{
		public String format(StringBuilder entry, JettyEvent event)
		{   
			Authentication authentication = event.request.getAuthentication();
			String username = (authentication instanceof Authentication.User) ? ((Authentication.User)authentication).getUserIdentity().getUserPrincipal().getName() : "-";
			entry.append(username);
			return child.format(entry, event);
		}
	}

	public String format(StringBuilder entry, Event event)
	{
			return this.format(entry, (JettyEvent) event);
	}

	public abstract String format(StringBuilder entry, JettyEvent event);
}
