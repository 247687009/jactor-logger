package championshang.wordpress.com.netty.server;

import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEventVO;

public class EchoServerHandler extends SimpleChannelHandler {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EchoServerHandler.class);
	private static final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	private final AtomicLong transferredBytes = new AtomicLong();
	
	public long getTransferredBytes() {
		return transferredBytes.get();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		// Send back the received message to the remote peer.
		LoggingEventVO event = ((LoggingEventVO) e.getMessage());		
		
		
		Logger  remoteLogger = lc.getLogger(event.getLoggerName());
	        // apply the logger-level filter
	        if (remoteLogger.isEnabledFor(event.getLevel())) {
	          // finally log the event as if was generated locally
	          remoteLogger.callAppenders(event);
	        }
		
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// Close the connection when an exception is raised.
		logger.warn("Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
}