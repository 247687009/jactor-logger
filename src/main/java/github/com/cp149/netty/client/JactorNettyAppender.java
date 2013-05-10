package github.com.cp149.netty.client;

import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author cp149 push logevent with actor
 */
public class JactorNettyAppender extends NettyAppender {
	private final MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);

	@Override
	protected void append(ILoggingEvent eventObject) {
		try {
			if (isStarted()) {
				if (connectatstart == false && bootstrap == null)
					connect(address, port);
				NettyActor actor = new NettyActor(eventObject, getChannel());
				actor.initialize(mailboxFactory.createMailbox());
				NettyRequest.req.sendEvent(actor);
			}

		} catch (Exception e) {
			addError(e.getMessage());
		}
	}

}
