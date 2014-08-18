package github.com.cp149.jactor2;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.SAOp;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

public class LoggerActor2 extends NonBlockingBladeBase {

	final AppenderAttachableImpl<ILoggingEvent> aai;

	public LoggerActor2(final AppenderAttachableImpl<ILoggingEvent> aai)
			throws Exception {
		super(new NonBlockingReactor());
		this.aai = aai;

	}

	public AOp<Void> printReq(final ILoggingEvent eventObject) {
		return new AOp<Void>("log4jappend", getReactor()) {
			

			@Override
			protected void processAsyncOperation(AsyncRequestImpl arg0,
					AsyncResponseProcessor<Void> arg1) throws Exception {
				aai.appendLoopOnAppenders(eventObject);
				arg1.processAsyncResponse(null);
				
			}
			
		};

	}

}
