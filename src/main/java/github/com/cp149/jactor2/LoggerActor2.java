package github.com.cp149.jactor2;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

public class LoggerActor2 extends NonBlockingBladeBase {

	final AppenderAttachableImpl<ILoggingEvent> aai;

	public LoggerActor2(final AppenderAttachableImpl<ILoggingEvent> aai)
			throws Exception {
		super(new NonBlockingReactor());
		this.aai = aai;

	}

	public AReq<Void> printReq(final ILoggingEvent eventObject) {
		return new AReq<Void>("log4jappend") {			

			@Override
			protected void processAsyncOperation(AsyncRequestImpl arg0,
					AsyncResponseProcessor<Void> arg1) throws Exception {
				aai.appendLoopOnAppenders(eventObject);
				arg1.processAsyncResponse(null);
				
			}
			
		};

	}

}
