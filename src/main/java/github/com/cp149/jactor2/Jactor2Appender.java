package github.com.cp149.jactor2;

import github.com.cp149.BaseAppender;

import org.agilewiki.jactor2.core.plant.Plant;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class Jactor2Appender extends BaseAppender {	
	LoggerActor2 actor1 ;
	@Override
	public void start() {

		super.start();
		new Plant(1);
		actor1= new LoggerActor2();	
		
	}

	@Override
	public void stop() {
		
		detachAndStopAllAppenders();
		super.stop();
		try {
			actor1.getReactor().close();
			actor1=null;
			Plant.close();			
		} catch (Exception e) {

		}
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		try {
			if (includeCallerData) {
				eventObject.prepareForDeferredProcessing();
				eventObject.getCallerData();
			}			
					
			try {
				actor1.new h1(eventObject, aai).call();
			} catch (Exception e) {
				addError(e.getMessage());
			}
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

	

}
