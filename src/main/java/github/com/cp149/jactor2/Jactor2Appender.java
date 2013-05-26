package github.com.cp149.jactor2;

import github.com.cp149.BaseAppender;

import org.agilewiki.jactor.api.Mailbox;
import org.agilewiki.jactor.api.MailboxFactory;
import org.agilewiki.jactor.impl.DefaultMailboxFactoryImpl;
import org.agilewiki.jactor.impl.ThreadManagerImpl;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class Jactor2Appender extends BaseAppender {
	final MailboxFactory mailboxFactory = new DefaultMailboxFactoryImpl( ThreadManagerImpl.newThreadManager(1));
    final Mailbox mailbox = mailboxFactory.createMailbox(true);
	
	private int threadSize = 2;
	
	@Override
	public void start() {

		super.start();
		
	}

	@Override
	public void stop() {
		try {
			mailboxFactory.close();
		} catch (Exception e) {
			
		}
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
			final LoggerActor2 actor1 = new LoggerActor2(mailbox,eventObject,this.aai);
			try {
				actor1.hi1.signal();
			} catch (Exception e) {
				
			}
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
