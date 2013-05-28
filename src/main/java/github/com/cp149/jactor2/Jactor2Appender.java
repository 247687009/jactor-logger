package github.com.cp149.jactor2;

import github.com.cp149.BaseAppender;

import org.agilewiki.jactor.api.Mailbox;
import org.agilewiki.jactor.api.MailboxFactory;
import org.agilewiki.jactor.impl.DefaultMailboxFactoryImpl;
import org.agilewiki.jactor.impl.ThreadManagerImpl;
import org.agilewiki.jactor.util.PASemaphore;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class Jactor2Appender extends BaseAppender {
	private static final int RINGBUFFER_DEFAULT_SIZE = 1024*1024;
	final MailboxFactory mailboxFactory = new DefaultMailboxFactoryImpl( ThreadManagerImpl.newThreadManager(1));
    final Mailbox mailbox = mailboxFactory.createMailbox();
    final github.com.cp149.jactor2.PASemaphore semaphore = new github.com.cp149.jactor2.PASemaphore(
    		new DefaultMailboxFactoryImpl( ThreadManagerImpl.newThreadManager(1)).createMailbox(), RINGBUFFER_DEFAULT_SIZE,RINGBUFFER_DEFAULT_SIZE);
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
			final LoggerActor2 actor1 = new LoggerActor2(mailbox,eventObject,this.aai,semaphore);
			try {				
				actor1.hi1.signal();
//				semaphore.acquireReq().call();
				
			} catch (Exception e) {
				addError(e.getMessage());
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
