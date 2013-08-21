package github.com.cp149.jactor2;

import github.com.cp149.BaseAppender;

import org.agilewiki.jactor2.core.context.JAContext;
import org.agilewiki.jactor2.core.processing.AtomicMessageProcessor;
import org.agilewiki.jactor2.core.processing.MessageProcessor;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class Jactor2Appender extends BaseAppender {	
	final JAContext jaContext = new JAContext(1);
    final MessageProcessor messageProcessor = new AtomicMessageProcessor(jaContext);
	
	
	@Override
	public void start() {

		super.start();
		
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
			final LoggerActor2 actor1 = new LoggerActor2(messageProcessor,eventObject,this.aai);
			try {				
				actor1.hi1.signal();				
			} catch (Exception e) {
				addError(e.getMessage());
			}
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}


}
