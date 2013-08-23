package github.com.cp149.jactor2;

import github.com.cp149.BaseAppender;

import org.agilewiki.jactor2.core.processing.MessageProcessor;
import org.agilewiki.jactor2.core.processing.NonBlockingMessageProcessor;
import org.agilewiki.jactor2.core.threading.ModuleContext;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class Jactor2Appender extends BaseAppender {
	ModuleContext jaContext;
	MessageProcessor messageProcessor;
	private int threadSize = 1;

	@Override
	public void start() {

		super.start();
		jaContext = new ModuleContext(1024, 1024, threadSize, new org.agilewiki.jactor2.core.threading.DefaultThreadFactory());
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
