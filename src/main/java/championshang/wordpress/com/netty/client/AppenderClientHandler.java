package championshang.wordpress.com.netty.client;

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
	// private org.jboss.netty.util.Timer timer=new HashedWheelTimer();
	private ClientBootstrap bootstrap;
	private final org.jboss.netty.util.Timer timer;
	private int reconnectDelay=30000;

	// org.jboss.netty.channel.Channel channel;

	public AppenderClientHandler(ClientBootstrap bootstrap2, org.jboss.netty.util.Timer timer,int timeout) {
		super();	
		this.bootstrap=bootstrap2;
		this.timer = timer;
		this.reconnectDelay=timeout;
		
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		// Send the first message. Server will not send anything here
		// because the firstMessage's capacity is 0.

		// this.channel=e.getChannel();
	}
	

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		timer.newTimeout(new TimerTask() {

			public void run(Timeout timeout) throws Exception {				
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
