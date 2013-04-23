package championshang.wordpress.com.netty.client;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import ch.qos.logback.classic.net.LoggingEventPreSerializationTransformer;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.PreSerializationTransformer;

public class NettyAppender extends NetAppenderBase<ILoggingEvent> {
	PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();
	protected ClientBootstrap bootstrap;
	protected int channelSize = 10;

	protected AppenderClientHandler appenderClientHandler = new AppenderClientHandler();
	protected final List<Channel> channelList = new ArrayList<Channel>();

	int channelid = 0;

	protected Channel getChannel() {
		if (channelid >= channelSize)
			channelid = 0;
		return channelList.get(channelid++);
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		try {
			if (isStarted()) {
				eventObject.prepareForDeferredProcessing();
				eventObject.getCallerData();
				Serializable serEvent = getPST().transform(eventObject);
				
				getChannel().write(serEvent);
			}

		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

	@Override
	public void cleanUp() {
		if (bootstrap != null) {
			for (Channel channel : channelList) {
				channel.close();
			}

			bootstrap.releaseExternalResources();
		}

	}

	@Override
	public void connect(InetAddress address, int port) {
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() throws Exception {

				return Channels.pipeline(new ObjectEncoder(), appenderClientHandler);
			}
		});
		for (int i = 0; i < channelSize; i++) {
			ChannelFuture future = bootstrap.connect(new InetSocketAddress(address, port));
			Channel channel = future.awaitUninterruptibly().getChannel();
			channelList.add(channel);
		}

	}

	@Override
	protected PreSerializationTransformer<ILoggingEvent> getPST() {

		return pst;
	}

}
