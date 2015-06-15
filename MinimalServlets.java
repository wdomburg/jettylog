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

import com.synacor.jetty.log.TxidFilter;
import com.synacor.jetty.log.TestRequestLog;
import com.synacor.jetty.log.CustomRequestLog;

public class MinimalServlets
{
    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);

        ServletHandler handler = new ServletHandler();
		handler.addFilterWithMapping(TxidFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        handler.addServletWithMapping(HelloServlet.class, "/*");

		//RequestLog requestLog = new CustomRequestLog("%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\" \"%{Cookie}i\" %{Syn-Txid}i %v %D \"%{HOST}i\" %P %{mod_php_memory_usage}n");
		RequestLog requestLog = new TestRequestLog();
		RequestLogHandler logHandler = new RequestLogHandler();
		logHandler.setRequestLog(requestLog);

		HandlerCollection handlers = new HandlerCollection();
		handlers.addHandler(handler);
		handlers.addHandler(logHandler);

        server.setHandler(handlers);

        server.start();
        server.join();
    }

    @SuppressWarnings("serial")
    public static class HelloServlet extends HttpServlet
    {
        @Override
        protected void doGet( HttpServletRequest request,
                              HttpServletResponse response ) throws ServletException,
                                                            IOException
        {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Hi.");
        }
    }
}
