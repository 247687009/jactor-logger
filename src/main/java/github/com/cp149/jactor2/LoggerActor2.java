package github.com.cp149.jactor2;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

public class LoggerActor2 extends NonBlockingBladeBase {



	public LoggerActor2() throws Exception {
		super(new NonBlockingReactor());
		
	}

	
    
	
	public AOp<Void> printReq(
			final AppenderAttachableImpl<ILoggingEvent> aai,
			final ILoggingEvent eventObject) {

		return new AOp<Void>(null, getReactor()) {				

			@Override
			public void processAsyncOperation(AsyncRequestImpl _AsyncRequestImpl, AsyncResponseProcessor<Void> _AsyncResponseProcessor) throws Exception {
				aai.appendLoopOnAppenders(eventObject);				
				_AsyncResponseProcessor.processAsyncResponse(null);
				
			}
		};
	}
	
	
	

}
