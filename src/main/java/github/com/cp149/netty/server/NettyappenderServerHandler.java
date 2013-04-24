package github.com.cp149.netty.server;

import github.com.cp149.netty.client.MyLoggingEventVO;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEventVO;

public class NettyappenderServerHandler extends SimpleChannelHandler {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(NettyappenderServerHandler.class);
	private static final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	private final AtomicLong transferredBytes = new AtomicLong();
	
	public long getTransferredBytes() {
		return transferredBytes.get();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		// Send back the received message to the remote peer.
		MyLoggingEventVO event = ((MyLoggingEventVO) e.getMessage());		
		
		
//		Logger  remoteLogger = lc.getLogger(event.getLoggerName());
//	        // apply the logger-level filter
//	        if (remoteLogger.isEnabledFor(event.getLevel())) {
//	          // finally log the event as if was generated locally
//	          remoteLogger.callAppenders(event);
//	        }
		logger.debug(event.getMsg());
//		
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// Close the connection when an exception is raised.
		if(!(e.getCause() instanceof IOException))
		logger.warn("Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
}