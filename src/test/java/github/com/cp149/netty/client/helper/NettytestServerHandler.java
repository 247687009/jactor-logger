package github.com.cp149.netty.client.helper;


import java.util.concurrent.LinkedBlockingDeque;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class NettytestServerHandler extends SimpleChannelHandler {
	

	public NettytestServerHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {		
		NettyTestInfo testInfo= (NettyTestInfo) e.getMessage();
		
		
	}

}
