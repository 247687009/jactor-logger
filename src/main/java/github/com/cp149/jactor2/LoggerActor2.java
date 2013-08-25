package github.com.cp149.jactor2;

import org.agilewiki.jactor2.core.ActorBase;
import org.agilewiki.jactor2.core.messaging.Request;
import org.agilewiki.jactor2.core.messaging.Transport;
import org.agilewiki.jactor2.core.processing.MessageProcessor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachableImpl;


public class LoggerActor2 extends ActorBase {
   
    public final Request<Void> hi1;

    public LoggerActor2(final MessageProcessor mbox, final ILoggingEvent eventObject, final AppenderAttachableImpl<ILoggingEvent> aai) throws Exception{   
    	 initialize(mbox);
        hi1 = new Request<Void>(getMessageProcessor()) {
            @Override
            public void processRequest(
                    final Transport<Void> responseProcessor)
                    throws Exception {
            	aai.appendLoopOnAppenders(eventObject);       
            	
            }
        };
    }
}
