package github.com.cp149.jactor;

import github.com.cp149.BaseAppender;

import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author cp149 jactor appender
 */
public class JactorAppender extends BaseAppender {

	private int threadSize = 2;
	private MailboxFactory mailboxFactory;
	private Mailbox mailbox;
	private final JAFuture future = new JAFuture();
	private static final int RINGBUFFER_DEFAULT_SIZE = 48 * 1024;
	@Override
	public void start() {

		super.start();
		mailboxFactory = JAMailboxFactory.newMailboxFactory(threadSize);
		mailbox = mailboxFactory.createMailbox();
		
	}

	@Override
	public void stop() {
		mailboxFactory.close();		
		detachAndStopAllAppenders();
		super.stop();
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		try {		
			if (includeCallerData) {
			eventObject.prepareForDeferredProcessing();
			eventObject.getCallerData();
			}
			LoggerActor actor = new LoggerActor(eventObject, aai);
			actor.initialize(mailbox);			
			ActorRequest.req.sendEvent(actor);
			
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

	public int getThreadSize() {
		return threadSize;
	}

	public void setThreadSize(int threadSize) {
		this.threadSize = threadSize;
	}

}
