package github.com.cp149;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;

public class CountAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
	public final static AtomicInteger count=new AtomicInteger();
	@Override
	protected void append(ILoggingEvent eventObject) {
		count.incrementAndGet();
		
	}

}
