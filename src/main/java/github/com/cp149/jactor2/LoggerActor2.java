package github.com.cp149.jactor2;

import org.agilewiki.jactor.api.Mailbox;
import org.agilewiki.jactor.api.Request;
import org.agilewiki.jactor.api.RequestBase;
import org.agilewiki.jactor.api.Transport;
import org.agilewiki.jactor.util.PASemaphore;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachableImpl;


public class LoggerActor2  {
   
    public final Request<Void> hi1;

    public LoggerActor2(final Mailbox mbox, final ILoggingEvent eventObject, final AppenderAttachableImpl<ILoggingEvent> aai) {   
    	
        hi1 = new RequestBase<Void>(mbox) {
            @Override
            public void processRequest(
                    final Transport<Void> responseProcessor)
                    throws Exception {
            	aai.appendLoopOnAppenders(eventObject);
                responseProcessor.processResponse(null);
            }
        };
    }
}
