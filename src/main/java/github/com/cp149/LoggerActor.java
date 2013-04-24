package github.com.cp149;

import java.util.Iterator;

import org.agilewiki.jactor.lpc.JLPCActor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

public class LoggerActor extends JLPCActor {
	private ILoggingEvent event;
	private AppenderAttachableImpl<ILoggingEvent> attachableImpl;

	

	public LoggerActor(ILoggingEvent event, AppenderAttachableImpl<ILoggingEvent> attachableImpl) {
		super();
		this.event = event;
		this.attachableImpl = attachableImpl;
	}



	public void doLogger() throws Exception {		
		attachableImpl.appendLoopOnAppenders(event);
		
	}

}
