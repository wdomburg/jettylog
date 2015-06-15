package com.synacor.jetty.log;

import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;

public abstract class JettyConverter extends Converter
{   
	public static BytesWritten bytesWritten() { return new BytesWritten(); }
	public static Latency latency() { return new Latency(); }
	public static Method method() { return new Method(); }
	public static Protocol protocol() { return new Protocol(); }
	public static RemoteAddress remoteAddress() { return new RemoteAddress(); }
	public static RequestString requestString() { return new RequestString(); }
	public static RequestUri requestUri() { return new RequestUri(); }
	public static Status status() { return new Status(); }
	public static TimeReceived timeReceived() { return new TimeReceived(); }
	public static Username username() { return new Username(); }

	//public static Converter header(final String header)
	//{
	//	return new JettyConverter()
	//	{
	//		public String format(StringBuilder entry, Request request, Response response)
	//		{
	//			String value = request.getHeader(header);
	//			entry.append(value != null ? value : "");
	//			return child.format(entry, request, response);
	//		}
	//	};
	//}

	public static Header header(String header) { return new Header(header); }

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

	// FIXME: support both %B (empty = 0) and %b (empty = -)
	private static class BytesWritten extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			// NOTE: This changes to .getHttpChannel().getBytesWritten() for Jetty 9.x
			long bytes = response.getContentCount();
			entry.append(bytes > 0 ? bytes : "-");
			return child.format(entry, request, response);
		}
	}

	private static class Latency extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			long now = System.currentTimeMillis();
			entry.append(now - request.getTimeStamp());
			return child.format(entry, request, response);
		}
	}

	private static class Method extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(request.getMethod());
			return child.format(entry, request, response);
		}
	}

	private static class Protocol extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(request.getProtocol());
			return child.format(entry, request, response);
		}
	}

	private static class RemoteAddress extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(request.getRemoteAddr());
			return child.format(entry, request, response);
		}
	}

	private static class RequestString extends JettyConverter
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

	private static class RequestUri extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(request.getRequestURI().toString());
			return child.format(entry, request, response);
		}
	}

	private static class Status extends JettyConverter
	{
		public String format(StringBuilder entry, Request request, Response response)
		{   
			entry.append(response.getStatus());
			return child.format(entry, request, response);
		}
	}

	private static class TimeReceived extends JettyConverter
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

	private static class Username extends JettyConverter
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
