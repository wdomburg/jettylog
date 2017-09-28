import com.synacor.util.txid.UuidFactory;
import com.synacor.jetty.log.CustomRequestLog;
import com.synacor.jetty.log.appender.Log4jAppender;
import com.synacor.jetty.log.txid.TxidHandlerWrapper;

import org.apache.log4j.PropertyConfigurator;

import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletHandler;

public class Log4j
{
    public static void main( String[] args ) throws Exception
    {

	PropertyConfigurator.configure("./log4j.properties");

        Server server = new Server(8080);

        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(HelloServlet.class, "/*");

	Log4jAppender appender = new Log4jAppender();

	RequestLog requestLog = new CustomRequestLog(appender);
	RequestLogHandler logHandler = new RequestLogHandler();
	logHandler.setRequestLog(requestLog);

	TxidHandlerWrapper txidHandler = new TxidHandlerWrapper(UuidFactory.class);
	txidHandler.setHandler(logHandler);

	HandlerCollection handlers = new HandlerCollection();
	handlers.addHandler(handler);
	handlers.addHandler(txidHandler);

        server.setHandler(handlers);

        server.start();
        server.join();
    }

}
