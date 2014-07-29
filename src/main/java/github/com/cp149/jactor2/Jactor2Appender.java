package github.com.cp149.jactor2;

import org.agilewiki.jactor2.core.impl.Plant;

import github.com.cp149.BaseAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class Jactor2Appender extends BaseAppender {	
	LoggerActor2 actor1 ;
	@Override
	public void start() {

		super.start();
		try {
			new Plant();		
			actor1= new LoggerActor2();
		} catch (Exception e) {			
			e.printStackTrace();
			addError(e.getMessage());
		}	
		
	}

	@Override
	public void stop() {
		
		detachAndStopAllAppenders();
		try {
			actor1.getReactor().close();
			actor1=null;
			Plant.close();			
		} catch (Exception e) {

		}
		super.stop();
		
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		try {
			if (includeCallerData) {
				eventObject.prepareForDeferredProcessing();
				eventObject.getCallerData();
			}			
					
			try {			
				
				actor1.printReq(aai, eventObject).call();
			} catch (Exception e) {
				addError(e.getMessage());
			}
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

	

}
