package com.synacor.jetty.log.converter;

import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;

public abstract class JettyConverter extends Converter
{   

	public static class Attribute extends JettyConverter
	{
		private final String name;

		public Attribute(String name)
		{
			this.name = name;
		}

		public String format(StringBuilder entry, Request request, Response response)
		{
			String value = (String) request.getAttribute(name);
			entry.append(value != null ? value : "-");
			return child.format(entry, request, response);
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
	
		public String format(StringBuilder entry, Request request, Response response)
		{   
			// NOTE: This changes to .getHttpChannel().getBytesWritten() for Jetty 9.x
			long bytes = response.getContentCount();
			entry.append(bytes > 0 ? bytes : empty);
			return child.format(entry, request, response);
		}
	}

	public static class Header extends JettyConverter
	{
		private final String header;

		public Header(String header)
		{
			this.header = header;
		}

		public String format(StringBuilder entry, Request request, Response response)
		{
			String value = request.getHeader(header);
			entry.append(value != null ? value : "");
			return child.format(entry, request, response);
		}
	}

	public static class Latency extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			long now = System.currentTimeMillis();
			entry.append(now - request.getTimeStamp());
			return child.format(entry, request, response);
		}
	}

	public static class LatencySeconds extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{
			long now = System.currentTimeMillis();
			entry.append(now - request.getTimeStamp() / 1000);
			return child.format(entry, request, response);
		}
	}

	public static class Method extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(request.getMethod());
			return child.format(entry, request, response);
		}
	}

	public static class Protocol extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(request.getProtocol());
			return child.format(entry, request, response);
		}
	}

	public static class RemoteAddress extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(request.getRemoteAddr());
			return child.format(entry, request, response);
		}
	}

	public static class RequestString extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(request.getMethod());
			entry.append(" ");
			entry.append(request.getRequestURI().toString());
			entry.append(" ");
			entry.append(request.getProtocol());
			return child.format(entry, request, response);
		}
	}

	public static class RequestUri extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(request.getRequestURI().toString());
			return child.format(entry, request, response);
		}
	}

	public static class RequestUrl extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(request.getRequestURL().toString());
			return child.format(entry, request, response);
		}
	}

	public static class Status extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(response.getStatus());
			return child.format(entry, request, response);
		}
	}

	public static class TimeReceived extends JettyConverter
	{
		private static final DateFormat dateFormat = new SimpleDateFormat("dd/MMM/YYYY:HH:mm:ss Z");

		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append("[");
			entry.append(dateFormat.format(request.getTimeStamp()));
			entry.append("]");
			return child.format(entry, request, response);
		}
	}

	public static class Username extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			Authentication authentication = request.getAuthentication();
			String username = (authentication instanceof Authentication.User) ? ((Authentication.User)authentication).getUserIdentity().getUserPrincipal().getName() : "-";
			entry.append(username);
			return child.format(entry, request, response);
		}
	}

    public String format(StringBuilder entry, Object ... args)
    {   
		Request request = (Request) args[0];
		Response response = (Response) args[1];

		return this.format(entry, request, response);
    }

	public abstract String format(StringBuilder entry, Request request, Response response);
}
