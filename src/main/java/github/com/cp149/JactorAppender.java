package github.com.cp149;

import java.util.Iterator;

import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

public class JactorAppender extends UnsynchronizedAppenderBase<ILoggingEvent> implements AppenderAttachable<ILoggingEvent> {

	AppenderAttachableImpl<ILoggingEvent> aai = new AppenderAttachableImpl<ILoggingEvent>();
	private final MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);

	public void addAppender(Appender<ILoggingEvent> newAppender) {

		aai.addAppender(newAppender);

	}

	public Iterator<Appender<ILoggingEvent>> iteratorForAppenders() {
		return aai.iteratorForAppenders();
	}

	public Appender<ILoggingEvent> getAppender(String name) {
		return aai.getAppender(name);
	}

	public boolean isAttached(Appender<ILoggingEvent> appender) {
		return aai.isAttached(appender);
	}

	public void detachAndStopAllAppenders() {
		aai.detachAndStopAllAppenders();

	}

	public boolean detachAppender(Appender<ILoggingEvent> appender) {
		return aai.detachAppender(appender);
	}

	public boolean detachAppender(String name) {
		return aai.detachAppender(name);
	}

	@Override
	public void stop() {
		mailboxFactory.close();
		Iterator<Appender<ILoggingEvent>> iteratorForAppenders = aai.iteratorForAppenders();
		while (iteratorForAppenders.hasNext()) {
			iteratorForAppenders.next().stop();
		}

		super.stop();
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		try {

			eventObject.prepareForDeferredProcessing();
			eventObject.getCallerData();

			LoggerActor actor = new LoggerActor(eventObject, aai);
			actor.initialize(mailboxFactory.createMailbox());

			ActorRequest.req.sendEvent(actor);
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

}
