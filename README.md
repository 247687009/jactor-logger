jactor-logger
=============

logback appender use jactor,another Async appender
easy to use,such as add append in logback.xml

```html
<appender name="ASYNC" class="github.com.cp149.JactorAppender">
		<appender-ref ref="STDOUT" />
</appender>
```
netty appender,send logs to netty server 
```html
<appender name="netty"
		class="github.com.cp149.netty.client.NettyAppender">
		<remoteHost>localhost</remoteHost>
		<port>4560</port>
		<appender-ref ref="STDOUT" />
		<connectatstart>true</connectatstart>
		<channelSize>20</channelSize>
	</appender>
```
put together
```html
<appender name="netty"
		class="github.com.cp149.netty.client.NettyAppender">
		<remoteHost>localhost</remoteHost>
		<port>4560</port>
		<appender-ref ref="STDOUT" />
		<connectatstart>true</connectatstart>
		<channelSize>20</channelSize>
	</appender>
<appender name="ASYNCNET" class="github.com.cp149.JactorAppender">
		<appender-ref ref="netty" />
</appender>

```