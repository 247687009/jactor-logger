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
1. Logback: Async disruptor Appender (single thread): throughput: 9,951,645 ops/sec. latency(ns): avg=5836.8 99% < 8192.0 99.99% < 40317747.2 (301733 samples)
2. Log4j2: Loggers all async (single thread): throughput: 9,286,567 ops/sec. latency(ns): avg=4537.4 99% < 7372.8 99.99% < 13513523.2 (636258 samples)
3. Logback: Async jactor Appender (single thread): throughput: 8,881,213 ops/sec. latency(ns): avg=1158.4 99% < 2048.0 99.99% < 52428.8 (23572385 samples)
4. Log4j2: Async Appender (single thread): throughput: 7,426,999 ops/sec. latency(ns): avg=820.6 99% < 4096.0 99.99% < 91750.4 (200265 samples)
5. Log4j2: Async Appender (2 threads): throughput: 3,751,904 ops/sec. latency(ns): avg=760.0 99% < 4096.0 99.99% < 72089.6 (1165310 samples)
6. Log4j2: Loggers all  Sync (2 threads): throughput: 3,684,754 ops/sec. latency(ns): avg=6704.4 99% < 6553.6 99.99% < 23592960.0 (1725538 samples)
7. Log4j2: Async Appender (4 threads): throughput: 3,046,964 ops/sec. latency(ns): avg=501.3 99% < 2048.0 99.99% < 88473.6 (22276330 samples)
8. Log4j2: Loggers all  Sync (4 threads): throughput: 2,713,546 ops/sec. latency(ns): avg=3802.8 99% < 4915.2 99.99% < 11403264.0 (6888406 samples)
9. Logback: Async jactor2 Appender (2 threads): throughput: 2,522,910 ops/sec. latency(ns): avg=1035.2 99% < 2048.0 99.99% < 65536.0 (36582271 samples)
10. Logback: Async disruptor Appender (2 threads): throughput: 2,521,034 ops/sec. latency(ns): avg=2618.0 99% < 4915.2 99.99% < 10190848.0 (2325082 samples)
11. Logback: Async disruptor Appender (4 threads): throughput: 2,075,186 ops/sec. latency(ns): avg=13528.8 99% < 6144.0 99.99% < 63871385.6 (4671829 samples)
12. Logback: Async jactor Appender (4 threads): throughput: 1,485,376 ops/sec. latency(ns): avg=915.8 99% < 1536.0 99.99% < 65536.0 (99159695 samples)
13. Logback: Async jactor2 Appender (4 threads): throughput: 1,473,195 ops/sec. latency(ns): avg=1216.7 99% < 1689.6 99.99% < 62259.2 (83900069 samples)
14. Logback: Async jactor2 Appender (single thread): throughput: 1,408,032 ops/sec. latency(ns): avg=1090.0 99% < 2048.0 99.99% < 58982.4 (13567361 samples)
15. Logback: Async jactor Appender (2 threads): throughput: 1,198,566 ops/sec. latency(ns): avg=927.9 99% < 2048.0 99.99% < 52428.8 (49802004 samples)

```

