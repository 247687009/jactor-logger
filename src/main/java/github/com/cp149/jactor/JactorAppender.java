package github.com.cp149.jactor;

import github.com.cp149.BaseAppender;

import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author cp149 jactor appender
 */
public class JactorAppender extends BaseAppender {

	private int threadSize = 2;
	private MailboxFactory mailboxFactory;

	@Override
	public void start() {

		super.start();
		mailboxFactory = JAMailboxFactory.newMailboxFactory(threadSize);

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
			actor.initialize(mailboxFactory.createMailbox());
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
