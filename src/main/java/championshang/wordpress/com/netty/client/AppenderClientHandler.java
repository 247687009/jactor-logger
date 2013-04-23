package championshang.wordpress.com.netty.client;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.LoggerFactory;

public class AppenderClientHandler extends SimpleChannelHandler {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AppenderClientHandler.class);
	
//	org.jboss.netty.channel.Channel  channel;

	public AppenderClientHandler() {
		super();
		
	}
	
	 @Override
	    public void channelConnected(
	            ChannelHandlerContext ctx, ChannelStateEvent e) {
	        // Send the first message.  Server will not send anything here
	        // because the firstMessage's capacity is 0.
		 	
//	        this.channel=e.getChannel();
	    }


	 
	 @Override
	    public void exceptionCaught(
	            ChannelHandlerContext ctx, ExceptionEvent e) {
	        // Close the connection when an exception is raised.
	        logger.error(                
	                "Unexpected exception from downstream.",
	                e.getCause());	        
	        e.getChannel().close();
	    }
}
