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
Ranking:
1. Logback: Async jactor Appender (single thread): throughput: 5,276,365 ops/sec. latency(ns): avg=710.0 99% < 2048.0 99.99% < 32768.0 (24269433 samples)
2. Log4j2: Async Appender (single thread): throughput: 4,851,906 ops/sec. latency(ns): avg=798.2 99% < 2048.0 99.99% < 32768.0 (24691479 samples)
3. Logback: Async jactor Appender (2 threads): throughput: 1,814,211 ops/sec. latency(ns): avg=603.1 99% < 2048.0 99.99% < 21299.2 (49936431 samples)
4. Log4j2: Async Appender (2 threads): throughput: 1,405,864 ops/sec. latency(ns): avg=815.6 99% < 7782.4 99.99% < 65536.0 (49893335 samples)
5. Logback: Async jactor Appender (4 threads): throughput: 1,160,528 ops/sec. latency(ns): avg=933.7 99% < 1843.2 99.99% < 16384.0 (99998700 samples)
6. Log4j2: Async Appender (4 threads): throughput: 788,836 ops/sec. latency(ns): avg=901.9 99% < 8192.0 99.99% < 36864.0 (99998671 samples)
7. Logback: Async disruptor Appender (single thread): throughput: 95,263 ops/sec. latency(ns): avg=644.2 99% < 8192.0 99.99% < 235929.6 (915749 samples)
8. Logback: Async disruptor Appender (2 threads): throughput: 50,524 ops/sec. latency(ns): avg=1216.2 99% < 2048.0 99.99% < 871628.8 (11424246 samples)
9. Logback: Async disruptor Appender (4 threads): throughput: 27,310 ops/sec. latency(ns): avg=471.3 99% < 1177.6 99.99% < 22937.6 (23364184 samples)