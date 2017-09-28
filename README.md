# jettylog

This is an alternate Jetty RequestLog implementation that providing for customizable output and integration with existing logging frameworks.

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

#### com.synacor.jetty.log.txid.TxidHandlerWrapper

