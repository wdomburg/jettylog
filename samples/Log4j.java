import java.io.IOException;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import com.synacor.jetty.log.txid.TxidFilter;

import com.synacor.jetty.log.CustomRequestLog;
import com.synacor.jetty.log.appender.Log4jAppender;

public class Log4j
{
    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);

        ServletHandler handler = new ServletHandler();
		handler.addFilterWithMapping(TxidFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        handler.addServletWithMapping(HelloServlet.class, "/*");

		Log4jAppender appender = new Log4jAppender();
		RequestLog requestLog = new CustomRequestLog(appender);
		RequestLogHandler logHandler = new RequestLogHandler();
		logHandler.setRequestLog(requestLog);

		HandlerCollection handlers = new HandlerCollection();
		handlers.addHandler(handler);
		handlers.addHandler(logHandler);

        server.setHandler(handlers);

        server.start();
        server.join();
    }

/*
    @SuppressWarnings("serial")
    public static class HelloServlet extends HttpServlet
    {
        @Override
        protected void doGet( HttpServletRequest request,
                              HttpServletResponse response ) throws ServletException,
                                                            IOException
        {
            response.setContentType("text/plain");

			String path = request.getPathInfo();

			switch(path)
			{
				case "/500":
            		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					break;
				case "/400":
            		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					break;
				default:
            		response.setStatus(HttpServletResponse.SC_OK);
			}

            response.getWriter().println(path);
        }
    }
*/
}
