package github.com.cp149.netty.server;

import github.com.cp149.disruptor.DisruptorAppender;
import github.com.cp149.disruptor.ValueEvent;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEventVO;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class NettyappenderServerHandler extends SimpleChannelHandler {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(NettyappenderServerHandler.class);
	private static final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	private final class LogEventHandler implements EventHandler<ValueEvent> {
		// event will eventually be recycled by the Disruptor after it wraps
		public void onEvent(final ValueEvent event, final long sequence, final boolean endOfBatch) throws Exception {
			
			Logger remoteLogger = lc.getLogger(event.getEvent().getLoggerName());
			// apply the logger-level filter
			if (remoteLogger.isEnabledFor(event.getEvent().getLevel())) {
				// finally log the event as if was generated locally
				remoteLogger.callAppenders(event.getEvent());
			}
			event.setEvent(null);
		}
	}

	
	ExecutorService exec = Executors.newFixedThreadPool(1);
	// Preallocate RingBuffer with 1024 ILoggingEvents
	Disruptor<ValueEvent> disruptor = new Disruptor<ValueEvent>(DisruptorAppender.EVENT_FACTORY, 1024, exec);
	final EventHandler<ValueEvent> handler = new LogEventHandler();

	public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
		public ValueEvent newInstance() {
			return new ValueEvent();
		}
	};
	private RingBuffer<ValueEvent> ringBuffer;
	

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {		
		super.channelConnected(ctx, e);
		disruptor.handleEventsWith(handler);
		ringBuffer = disruptor.start();
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelClosed(ctx, e);
		disruptor.shutdown();
		exec.shutdown();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		// Send back the received message to the remote peer.
		LoggingEventVO event = ((LoggingEventVO) e.getMessage());
		Logger remoteLogger = lc.getLogger(event.getLoggerName());
		// apply the logger-level filter
		if (remoteLogger.isEnabledFor(event.getLevel())) {
			// finally log the event as if was generated locally
			remoteLogger.callAppenders(event);
		}
		
		
//		long seq = ringBuffer.next();
//		ValueEvent valueEvent = ringBuffer.get(seq);
//		valueEvent.setEvent(event);
//		ringBuffer.publish(seq);
		

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// Close the connection when an exception is raised.
		if (!(e.getCause() instanceof IOException))
			logger.warn("Unexpected exception from downstream.", e.getCause());
		disruptor.shutdown();
		exec.shutdown();
		e.getChannel().close();
	}
}