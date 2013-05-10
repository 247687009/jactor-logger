package github.com.cp149.netty.client;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import org.jboss.netty.handler.codec.marshalling.MarshallerProvider;
import org.jboss.netty.handler.codec.marshalling.MarshallingEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import ch.qos.logback.classic.net.LoggingEventPreSerializationTransformer;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.PreSerializationTransformer;

public class NettyAppender extends NetAppenderBase<ILoggingEvent> {
	protected PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();
	protected ClientBootstrap bootstrap = null;
	protected int channelSize = 5;

	protected AppenderClientHandler appenderClientHandler;
	protected static final Timer timer = new HashedWheelTimer();
	protected Channel[] channelList;
	private Channel channel;

	int channelid = 0;

	protected Channel getChannel() {
		if (channelid >= channelSize)
			channelid = 0;
		return channelList[channelid++];
		// return channel;
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		try {
			if (isStarted()) {
				// if not start then start bootstrap
				if (connectatstart == false && bootstrap == null)
					connect(address, port);
				// eventObject.prepareForDeferredProcessing();
				eventObject.getCallerData();
				Serializable serEvent = getPST().transform(eventObject);
				// if connect write to server
				Channel channel = getChannel();
				if (channel.isConnected())
					channel.write(serEvent);
				else
					// else write to local
					aai.appendLoopOnAppenders(eventObject);

			}

		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

	@Override
	public void cleanUp() {
		try {
			if (bootstrap != null) {
				for (int i = 0; i < channelList.length; i++) {
					channelList[i].disconnect().awaitUninterruptibly();
					channelList[i].close();
				}

				// channel.disconnect().awaitUninterruptibly();
				// channel.close();
				bootstrap.releaseExternalResources();
				bootstrap.shutdown();
				bootstrap = null;
			}
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

	protected MarshallerFactory createMarshallerFactory() {
		return Marshalling.getProvidedMarshallerFactory("serial");
	}

	protected MarshallingConfiguration createMarshallingConfig() {
		// Create a configuration
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		return configuration;
	}

	protected MarshallerProvider createProvider() {
		return new DefaultMarshallerProvider(createMarshallerFactory(), createMarshallingConfig());
	}

	@Override
	public synchronized void connect(InetAddress address, int port) {
		if (bootstrap == null) {
			bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newFixedThreadPool(channelSize), Executors.newFixedThreadPool(channelSize)));
			bootstrap.setOption("tcpNoDelay", true);
			bootstrap.setOption("keepAlive", true);
			bootstrap.setOption("remoteAddress", new InetSocketAddress(address, port));
			final ExecutionHandler executionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(channelSize, 1024 * 1024*100, 1024 * 1024*100*channelSize));
//			bootstrap.setOption("writeBufferHighWaterMark", 10 * 64 * 1024);
			bootstrap.setOption("sendBufferSize", 1048576*100 );

			// Set up the pipeline factory.
			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

				public ChannelPipeline getPipeline() throws Exception {

					return Channels.pipeline(executionHandler, new MarshallingEncoder(createProvider()), new AppenderClientHandler(NettyAppender.this, bootstrap, timer,
							reconnectionDelay));
				}
			});
			channelList = new Channel[channelSize];
			for (int i = 0; i < channelSize; i++) {
				ChannelFuture future = bootstrap.connect().awaitUninterruptibly();
				channel = future.getChannel();
				channel.setReadable(false);

				channelList[i] = channel;
			}
		}

	}

	@Override
	protected PreSerializationTransformer<ILoggingEvent> getPST() {

		return pst;
	}

}
