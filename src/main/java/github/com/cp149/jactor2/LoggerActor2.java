package github.com.cp149.jactor2;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.SyncRequest;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

public class LoggerActor2 extends NonBlockingBladeBase {

	public LoggerActor2() {
		super();

	}

	public SyncRequest<Void> printReq(
			final AppenderAttachableImpl<ILoggingEvent> aai,
			final ILoggingEvent eventObject) {

		return new SyncBladeRequest<Void>() {	


			@Override
			public Void processSyncRequest() throws Exception {
				aai.appendLoopOnAppenders(eventObject);
				return null;
			}
		};
	}
	
	
	

}
