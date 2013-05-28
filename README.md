jactor-logger
=============

logback appender use jactor,another Async appender
easy to use,such as add append in logback.xml

```html
<appender name="ASYNC" class="github.com.cp149.jactor.JactorAppender">
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
<appender name="ASYNCNET" class="github.com.cp149.jactor.JactorAppender">
		<appender-ref ref="netty" />
</appender>

```

```html
Ranking:
1. Logback: Async disruptor Appender (single thread): throughput: 12,358,997 ops/sec. latency(ns): avg=817.6 99% < 5734.4 99.99% < 65536.0 (1864241 samples)
2. Logback: Async jactor Appender (single thread): throughput: 10,609,816 ops/sec. latency(ns): avg=780.2 99% < 2048.0 99.99% < 32768.0 (19479097 samples)
3. Log4j2: Loggers all async (single thread): throughput: 10,108,214 ops/sec. latency(ns): avg=1196.4 99% < 5734.4 99.99% < 65536.0 (2392987 samples)
4. Log4j2: Async Appender (single thread): throughput: 8,401,083 ops/sec. latency(ns): avg=648.4 99% < 8192.0 99.99% < 65536.0 (616363 samples)
5. Logback: Async disruptor Appender (2 threads): throughput: 4,968,991 ops/sec. latency(ns): avg=567.8 99% < 1843.2 99.99% < 26214.4 (11641379 samples)
6. Log4j2: Async Appender (2 threads): throughput: 3,945,138 ops/sec. latency(ns): avg=672.0 99% < 8192.0 99.99% < 91750.4 (2861566 samples)
7. Logback: Async jactor2 Appender (4 threads): throughput: 3,410,098 ops/sec. latency(ns): avg=710.3 99% < 1024.0 99.99% < 8192.0 (83727392 samples)
8. Log4j2: Loggers all  Sync (2 threads): throughput: 3,392,813 ops/sec. latency(ns): avg=586.9 99% < 2048.0 99.99% < 49152.0 (11866973 samples)
9. Logback: Async jactor2 Appender (2 threads): throughput: 3,294,824 ops/sec. latency(ns): avg=503.2 99% < 2048.0 99.99% < 16384.0 (20504661 samples)
10. Logback: Async jactor2 Appender (single thread): throughput: 3,272,595 ops/sec. latency(ns): avg=573.0 99% < 2048.0 99.99% < 32768.0 (8861775 samples)
11. Log4j2: Loggers all  Sync (4 threads): throughput: 3,237,113 ops/sec. latency(ns): avg=713.0 99% < 1024.0 99.99% < 18841.6 (40574503 samples)
12. Logback: Async disruptor Appender (4 threads): throughput: 2,731,884 ops/sec. latency(ns): avg=685.1 99% < 1024.0 99.99% < 15974.4 (25614599 samples)
13. Logback: Async jactor Appender (2 threads): throughput: 2,480,663 ops/sec. latency(ns): avg=565.4 99% < 2048.0 99.99% < 19660.8 (45254375 samples)
14. Log4j2: Async Appender (4 threads): throughput: 2,051,807 ops/sec. latency(ns): avg=427.4 99% < 1024.0 99.99% < 11878.4 (37606585 samples)
15. Logback: Async jactor Appender (4 threads): throughput: 1,563,293 ops/sec. latency(ns): avg=585.1 99% < 2048.0 99.99% < 9420.8 (97661429 samples)

```

