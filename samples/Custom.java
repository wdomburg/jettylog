import java.io.IOException;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import com.synacor.jetty.log.CustomRequestLog;
import com.synacor.jetty.log.appender.Appender;
import com.synacor.jetty.log.appender.PrintStreamAppender;
import com.synacor.jetty.log.layout.Layout;
import com.synacor.jetty.log.layout.PatternLayout;
import com.synacor.jetty.log.txid.TxidFilter;

public class Custom
{
    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);

        ServletHandler handler = new ServletHandler();
		handler.addFilterWithMapping(TxidFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        handler.addServletWithMapping(HelloServlet.class, "/*");

		String format = System.getProperty("com.synacor.jetty.log.format");
		if (format == null)
			format = "%h %l %u %t \"%r\" %s %b \"%{Referer}i\" \"%{User-Agent}i\" \"%{Cookie}i\" %{X-Transaction-ID}i %v %D \"%{HOST}i\" %P %{mod_php_memory_usage}n";

		Layout layout = new PatternLayout(format);
		Appender appender = new PrintStreamAppender(layout);
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
}
