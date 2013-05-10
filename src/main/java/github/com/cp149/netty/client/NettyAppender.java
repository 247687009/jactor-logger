package github.com.cp149.netty.client;

import github.com.cp149.netty.server.MarshallUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.udt.nio.NioUdtByteConnectorChannel;
import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.ThreadLocalMarshallerProvider;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import ch.qos.logback.classic.net.LoggingEventPreSerializationTransformer;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.PreSerializationTransformer;

public class NettyAppender extends NetAppenderBase<ILoggingEvent> {
	protected PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();
	protected Bootstrap bootstrap = null;
	protected EventLoopGroup group;
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
				if (channel.isActive())
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
				bootstrap.shutdown();
				group.shutdownGracefully();
				bootstrap = null;
			}
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

	@Override
	public synchronized void connect(InetAddress address, int port) {
		if (bootstrap == null) {
			final EventExecutorGroup executor = new DefaultEventExecutorGroup(8);
			bootstrap = new Bootstrap();
			group = new NioEventLoopGroup();
			
			bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.SO_RCVBUF, 20).option(ChannelOption.SO_SNDBUF, 46390)
					.remoteAddress(new InetSocketAddress(address, port)).handler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						public void initChannel(NioSocketChannel ch) throws Exception {
							ch.pipeline().addLast(executor, new MarshallingEncoder(MarshallUtil.createProvider()));
							// ch.pipeline().addLast(executor, new
							// AppenderClientHandler());
						}
					});

			// // Start the client.
			// ChannelFuture f = b.connect(host, port).sync();
			// bootstrap = new ClientBootstrap(new
			// NioClientSocketChannelFactory(Executors.newFixedThreadPool(channelSize),
			// Executors.newFixedThreadPool(channelSize)));
			// bootstrap.setOption("tcpNoDelay", true);
			// bootstrap.setOption("keepAlive", true);
			// bootstrap.setOption("remoteAddress", new
			// InetSocketAddress(address, port));
			//
			// bootstrap.setOption("sendBufferSize", 1048576*100 );
			//
			// // Set up the pipeline factory.
			// bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			//
			// public ChannelPipeline getPipeline() throws Exception {
			//
			// return Channels.pipeline(executionHandler, new
			// MarshallingEncoder(createProvider()), new
			// AppenderClientHandler(NettyAppender.this, bootstrap, timer,
			// reconnectionDelay));
			// }
			// });
			channelList = new Channel[channelSize];
			for (int i = 0; i < channelSize; i++) {
				ChannelFuture future = bootstrap.connect();
				channel = future.channel();
				channelList[i] = channel;
			}
		}

	}

	@Override
	protected PreSerializationTransformer<ILoggingEvent> getPST() {

		return pst;
	}

}
