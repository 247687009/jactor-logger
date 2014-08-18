package github.com.cp149.jactor2;

import github.com.cp149.BaseAppender;

import org.agilewiki.jactor2.core.impl.Plant;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class Jactor2Appender extends BaseAppender {	
	LoggerActor2 actor1 ;
	int count=0;
	@Override
	public void start() {

		super.start();
		try {
			new Plant(1);		
			actor1= new LoggerActor2(aai);
		} catch (Exception e) {			
			e.printStackTrace();
			addError(e.getMessage());
		}	
		
	}

	@Override
	public void stop() {
		
		detachAndStopAllAppenders();
		try {
			//while(!actor1.getReactor().isInboxEmpty()) TimeUnit.SECONDS.sleep(1);;
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
//				if(count++>=10000){					
//					count=0;					
					actor1.printReq( eventObject).call();
//				}
//				else
//				actor1.printReq( eventObject).signal();
			} catch (Exception e) {
				addError(e.getMessage());
			}
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

	

}
