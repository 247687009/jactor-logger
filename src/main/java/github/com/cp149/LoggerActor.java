package github.com.cp149;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

public class LoggerActor extends JLPCActor {
	private ILoggingEvent event;
	private AppenderAttachableImpl<ILoggingEvent> attachableImpl;

	

	public LoggerActor(ILoggingEvent event, AppenderAttachableImpl<ILoggingEvent> attachableImpl) {
		super();
		this.event = event;
		this.attachableImpl = attachableImpl;
	}

	
	 protected void processRequest(ActorRequest ar, RP rp) throws Exception {
		 attachableImpl.appendLoopOnAppenders(event);
	        rp.processResponse(null); //all done
	  }

}
