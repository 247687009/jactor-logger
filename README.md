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
1. Logback: Async disruptor Appender (single thread): throughput: 12,829,643 ops/sec. latency(ns): avg=982.8 99% < 8192.0 99.99% < 65536.0 (1469193 samples)
2. Logback: Async jactor Appender (single thread): throughput: 10,541,797 ops/sec. latency(ns): avg=802.0 99% < 2048.0 99.99% < 39321.6 (19612601 samples)
3. Log4j2: Loggers all async (single thread): throughput: 10,122,556 ops/sec. latency(ns): avg=2043.6 99% < 8192.0 99.99% < 6776422.4 (1474316 samples)
4. Log4j2: Async Appender (single thread): throughput: 8,414,090 ops/sec. latency(ns): avg=655.8 99% < 8192.0 99.99% < 65536.0 (783026 samples)
5. Logback: Async jactor2 Appender (single thread): throughput: 2,880,128 ops/sec. latency(ns): avg=727.2 99% < 2048.0 99.99% < 52428.8 (13755206 samples)


Ranking:
1. Log4j2: Async Appender (2 threads): throughput: 4,097,813 ops/sec. latency(ns): avg=629.7 99% < 8192.0 99.99% < 85196.8 (4063339 samples)
2. Logback: Async disruptor Appender (2 threads): throughput: 3,931,633 ops/sec. latency(ns): avg=601.2 99% < 2048.0 99.99% < 55705.6 (9362364 samples)
3. Log4j2: Loggers all  Sync (2 threads): throughput: 3,171,371 ops/sec. latency(ns): avg=716.9 99% < 7782.4 99.99% < 131072.0 (4760109 samples)
4. Log4j2: Loggers all  Sync (4 threads): throughput: 2,691,926 ops/sec. latency(ns): avg=837.4 99% < 1484.8 99.99% < 18022.4 (34229754 samples)
5. Logback: Async disruptor Appender (4 threads): throughput: 2,629,836 ops/sec. latency(ns): avg=685.2 99% < 1484.8 99.99% < 16384.0 (27521965 samples)
6. Logback: Async jactor2 Appender (2 threads): throughput: 2,541,449 ops/sec. latency(ns): avg=711.1 99% < 2048.0 99.99% < 36044.8 (23907069 samples)
7. Logback: Async jactor Appender (2 threads): throughput: 2,347,011 ops/sec. latency(ns): avg=593.1 99% < 2048.0 99.99% < 31129.6 (47267936 samples)
8. Log4j2: Async Appender (4 threads): throughput: 2,092,758 ops/sec. latency(ns): avg=425.6 99% < 1024.0 99.99% < 9420.8 (31496990 samples)
9. Logback: Async jactor2 Appender (4 threads): throughput: 1,903,440 ops/sec. latency(ns): avg=502.3 99% < 2048.0 99.99% < 16384.0 (75690569 samples)
10. Logback: Async jactor Appender (4 threads): throughput: 1,072,450 ops/sec. latency(ns): avg=525.0 99% < 2048.0 99.99% < 14745.6 (95831503 samples)

Ranking:
1. Log4j2: Async Appender (2 threads): throughput: 4,216,894 ops/sec. latency(ns): avg=624.6 99% < 8192.0 99.99% < 65536.0 (3593594 samples)
2. Log4j2: Loggers all  Sync (4 threads): throughput: 4,085,650 ops/sec. latency(ns): avg=786.1 99% < 2048.0 99.99% < 28672.0 (33609421 samples)
3. Logback: Async disruptor Appender (2 threads): throughput: 3,767,659 ops/sec. latency(ns): avg=550.0 99% < 2048.0 99.99% < 49152.0 (9901631 samples)
4. Logback: Async disruptor Appender (4 threads): throughput: 2,927,143 ops/sec. latency(ns): avg=777.0 99% < 2048.0 99.99% < 29491.2 (26962080 samples)
5. Log4j2: Loggers all  Sync (2 threads): throughput: 2,352,636 ops/sec. latency(ns): avg=815.8 99% < 7782.4 99.99% < 131072.0 (4411886 samples)
6. Logback: Async jactor2 Appender (2 threads): throughput: 2,336,805 ops/sec. latency(ns): avg=708.6 99% < 2048.0 99.99% < 32768.0 (25354170 samples)
7. Log4j2: Async Appender (4 threads): throughput: 2,090,157 ops/sec. latency(ns): avg=429.5 99% < 1024.0 99.99% < 14336.0 (30979857 samples)
8. Logback: Async jactor Appender (2 threads): throughput: 1,931,381 ops/sec. latency(ns): avg=593.7 99% < 2048.0 99.99% < 32768.0 (46924404 samples)
9. Logback: Async jactor Appender (4 threads): throughput: 1,310,619 ops/sec. latency(ns): avg=509.8 99% < 2048.0 99.99% < 15872.0 (76358725 samples)
10. Logback: Async jactor2 Appender (4 threads): throughput: 1,287,094 ops/sec. latency(ns): avg=523.0 99% < 2048.0 99.99% < 16384.0 (69991837 samples)

```

