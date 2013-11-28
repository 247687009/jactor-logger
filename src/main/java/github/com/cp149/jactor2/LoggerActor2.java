package github.com.cp149.jactor2;

import org.agilewiki.jactor2.core.messages.SyncRequest;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachableImpl;


public class LoggerActor2  {
	 private final Reactor reactor;
    public final SyncRequest<Void> hi1;

    public LoggerActor2(final IsolationReactor mbox, final ILoggingEvent eventObject, final AppenderAttachableImpl<ILoggingEvent> aai) throws Exception{   
    	reactor=(mbox);
        hi1 = new SyncRequest<Void>(reactor) {
            

			@Override
			protected Void processSyncRequest() throws Exception {
				aai.appendLoopOnAppenders(eventObject);       
            	return null;
			}
        };
    }
}
