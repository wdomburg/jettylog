# jettylog

This is an alternate Jetty RequestLog implementation that providing for customizable output and integration with existing logging frameworks.

## Usage

This framework can be utilized either through configuration files in the Jetty IoC XML format, or programatically.

### jetty.xml

To configure the use of log4j for the request log for the entire Jetty Server instance.

    <Set name="handler">
      <New id="Handlers" class="org.eclipse.jetty.server.handler.HandlerCollection">
        <Set name="handlers">
          <Array type="org.eclipse.jetty.server.Handler">
            <Item>
              <New id="Contexts" class="org.eclipse.jetty.server.handler.ContextHandlerCollection"/>
            </Item>
            <Item>
              <New id="DefaultHandler" class="org.eclipse.jetty.server.handler.DefaultHandler"/>
            </Item>
            <Item>
              <New id="RequestLog" class="org.eclipse.jetty.server.handler.RequestLogHandler"/>
            </Item>
          </Array>
        </Set>
      </New>
    </Set>
    <Ref id="RequestLog">
      <Set name="requestLog">
        <New id="RequestLogImpl" class="com.synacor.jetty.log.CustomRequestLog">
          <Arg>
            <New id="Appender" class="com.synacor.jetty.log.appender.Log4jAppender"></New>
          </Arg> 
      </Set>
    </Ref>

Alternately, to configure asynchronous logging without an additional dependency:

    <Ref id="RequestLog">
      <Set name="requestLog">
        <New id="RequestLogImpl" class="com.synacor.jetty.log.CustomRequestLog">
          <Arg>
            <New id="Appender" class="com.synacor.jetty.log.appender.AsyncAppender">
              <Arg>
                <New id="Appender" class="com.synacor.jetty.log.appender.DailyFileAppender">
                  <Arg>/opt/zimbra/log/access_log</Arg>
                  <Arg>
                     <New id="Layout" class="com.synacor.jetty.log.layout.PatternLayout">
                        <Arg>%h %l %u %t "%r" %s %b</Arg>
                     </New>
                  </Arg>
                </New>
              </Arg>
            </New>
          </Arg>
        </New>
      </Set>
    </Ref>

### Programatic

The equilalent code for the log4j example above would be:

    HandlerCollection handlers = new HandlerCollection();
    ContextHandlerCollection contexts = new ContextHandlerCollection();
    RequestLogHandler requestLogHandler = new RequestLogHandler();
    handlers.setHandlers(new Handler[]{contexts,new DefaultHandler(),requestLogHandler});
    server.setHandler(handlers);

    Log4jAppender appender = new Log4jAppender();
    RequestLog requestLog = new CustomRequestLog(appender);
    requstLogHandler.setRequestLog(requestLog);

## Components

### Event

An event is a discrete loggable event.  Within the Jetty framework this is any completed request, and encapsulates the request, response and information about the handling thread.

### Layout

Formats an event into a loggable string.

#### PatternLayout

Formats an event according to an Apache style pattern string, similar to the mod_log_config module of the Apache HTTP Server project.

| Format String | Description
| ------------- | ----------------------------------------------- |
| %b            | bytes written ('-' if empty)                    |
| %B            | bytes written (0 if empty)                      |
| %D            | time to service (ms)                            |
| %h            | remote address                                  |
| %{header}i    | request header                                  |
| %l            | logname (recognized but unimplemented)          |
| %m            | request method                                  |
| %{attribute}n | request attribute                               |
| %r            | request string                                  |
| %P            | thread servicing request                        |
| %s            | request status                                  |
| %t            | time received                                   |
| %T            | time to service request (s)                     |
| %u            | remote username                                 |
| %U            | request url                                     |
| %v            | canonical server (recognized but unimplemented) |

### Appender

Writes out a formatted logging event, as to a file or database.

#### com.synacor.jetty.log.FileAppender

Writes events to a file.

#### com.synacor.jetty.log.DailyFileAppender

A superclass of the FileAppender that adds daily file rotation.

#### com.synacor.jetty.log.AsyncAppender

Writes events to a queue and writes them asynchronously to a backing logger.

#### com.synacor.jetty.log.Log4jLineAppender

Writes a formatted entry to a log4j logger. Typically the Log4jAppender will be used instead.

#### com.synacor.jetty.log.Log4jAppender

Passes an unformatted log event to a log4j logger.

Successful operations will be logged as informational, client errors will be logged as warnings, and server errors will be logged as errors.

### Log4j Integration

#### com.synacor.jetty.log4j.JettyLayout

A log4j layout implementation to be used in concert with the Log4jAppender; e.g.

    log4j.appender.ERRORLOG=org.apache.log4j.ConsoleAppender
    log4j.appender.ERRORLOG=org.apache.log4j.RollingFileAppender
    log4j.appender.ERRORLOG.File=/var/log/myapp.error.log
    log4j.appender.ERRORLOG.layout=com.synacor.jetty.log4j.JettyLayout
    log4j.appender.ERRORLOG.layout.Pattern=%h %l %u %t "%r" %s %b "%{Referer}i" "%{User-Agent}i" "%{Cookie}i" "%{X-Transaction-ID}i" %v %D "%{HOST}i"
    log4j.appender.ERRORLOG.Threshold=WARN

### Other

Additional classes were added to this package to fascilitate the implementation of transaction ID generation and logging for tracability purposes.

#### TxidFactory

A utility class for generating transaction IDs for request tracking, along with two implementations.

#### HeaderMapRequestWrapper

A utility class for Jetty allowing injection of synthetic headers intp an HttpServletRequest.

#### TxidHandlerWrapper

A Jetty handler wrapper that injects a generated transaction ID into the headers of requests if not already present.


