package github.com.cp149.jactor2;

import github.com.cp149.BaseAppender;

import org.agilewiki.jactor2.core.context.DefaultThreadFactory;
import org.agilewiki.jactor2.core.context.JAContext;
import org.agilewiki.jactor2.core.processing.AtomicMessageProcessor;
import org.agilewiki.jactor2.core.processing.MessageProcessor;
import org.agilewiki.jactor2.core.processing.NonBlockingMessageProcessor;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class Jactor2Appender extends BaseAppender {
	JAContext jaContext;
	MessageProcessor messageProcessor;
	private int threadSize = 1;

	@Override
	public void start() {

		super.start();
		jaContext = new JAContext(1024, 1024, threadSize, new DefaultThreadFactory());
		messageProcessor = new NonBlockingMessageProcessor(jaContext);
	}

	@Override
	public void stop() {
		try {
			jaContext.close();
		} catch (Exception e) {

		}
		detachAndStopAllAppenders();
		super.stop();
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		try {
			if (includeCallerData) {
				eventObject.prepareForDeferredProcessing();
				eventObject.getCallerData();
			}
			LoggerActor2 actor1 = new LoggerActor2(messageProcessor, eventObject, this.aai);
			try {
				actor1.hi1.signal();
			} catch (Exception e) {
				addError(e.getMessage());
			}
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

	public int getThreadSize() {
		return threadSize;
	}

	public void setThreadSize(int threadSize) {
		this.threadSize = threadSize;
	}

}
