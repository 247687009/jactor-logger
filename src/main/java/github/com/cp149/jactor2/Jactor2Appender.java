package github.com.cp149.jactor2;

import github.com.cp149.BaseAppender;

import org.agilewiki.jactor2.core.facilities.DefaultThreadFactory;
import org.agilewiki.jactor2.core.facilities.Facility;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class Jactor2Appender extends BaseAppender {
	 Facility facility;
	
	private IsolationReactor isolationReactor;
	

	@Override
	public void start() {

		super.start();
		facility = new Facility(1024*1024, 1024*1024, 1, new DefaultThreadFactory());
		isolationReactor= new IsolationReactor(facility);
		
	}

	@Override
	public void stop() {
		try {
			facility.close();			
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
			LoggerActor2 actor1 = new LoggerActor2(isolationReactor, eventObject, this.aai);			
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
