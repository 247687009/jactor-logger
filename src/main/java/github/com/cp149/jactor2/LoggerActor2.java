package github.com.cp149.jactor2;

import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

public class LoggerActor2 extends NonBlockingBladeBase {

	class

	h1 extends AsyncBladeRequest<Void> {

		private final AppenderAttachableImpl<ILoggingEvent> aai;
		private final ILoggingEvent eventObject;

		public h1(final ILoggingEvent eventObject, final AppenderAttachableImpl<ILoggingEvent> aai) {
			super();
			this.aai = aai;
			this.eventObject = eventObject;
		}

		@Override
		public void processAsyncRequest() throws Exception {
			aai.appendLoopOnAppenders(eventObject);

		}

	}
}
