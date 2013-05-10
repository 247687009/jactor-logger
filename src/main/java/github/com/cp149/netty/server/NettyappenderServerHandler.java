package github.com.cp149.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;

import java.io.IOException;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEventVO;

public class NettyappenderServerHandler extends ChannelInboundMessageHandlerAdapter<LoggingEventVO> {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(NettyappenderServerHandler.class);
	private static final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		// Close the connection when an exception is raised.
		if (!(e.getCause() instanceof IOException))
			logger.warn("Unexpected exception from downstream.", e.getCause());

		ctx.close();
	}

	@Override
	public void messageReceived(ChannelHandlerContext arg0, LoggingEventVO event) throws Exception {
		// Send back the received message to the remote peer.
		// LoggingEventVO event = ((LoggingEventVO) e.getMessage());
		Logger remoteLogger = lc.getLogger(event.getLoggerName());
		// apply the logger-level filter
		if (remoteLogger.isEnabledFor(event.getLevel())) {
			event.getCallerData();
			// finally log the event as if was generated locally
			remoteLogger.callAppenders(event);
		}

	}
}