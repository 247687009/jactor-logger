package github.com.cp149.netty.client;

import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;
import org.slf4j.LoggerFactory;

public class AppenderClientHandler extends SimpleChannelHandler {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AppenderClientHandler.class);
	
	private ClientBootstrap bootstrap;
	private final org.jboss.netty.util.Timer timer;
	private int reconnectDelay=30000;
	private final NettyAppender appender;

	// org.jboss.netty.channel.Channel channel;

	public AppenderClientHandler(NettyAppender nettyAppender, ClientBootstrap bootstrap2, org.jboss.netty.util.Timer timer,int timeout) {
		super();	
		this.bootstrap=bootstrap2;
		this.timer = timer;
		this.reconnectDelay=timeout;
		this.appender=nettyAppender;
	}

	
	

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		timer.newTimeout(new TimerTask() {

			public void run(Timeout timeout) throws Exception {
				if(appender.isStarted())
				bootstrap.connect();
			}
		}, reconnectDelay, TimeUnit.SECONDS);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// Close the connection when an exception is raised.
		logger.error("Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}

	
}
