/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package github.com.cp149.netty.server;

import java.io.File;
import java.net.InetSocketAddress;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import org.jboss.netty.handler.codec.marshalling.MarshallingDecoder;
import org.jboss.netty.handler.codec.marshalling.UnmarshallerProvider;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * Echoes back any received data from a client.
 */
public class NettyappenderServer {

	private final int port;
	private ServerBootstrap bootstrap;

	public ServerBootstrap getBootstrap() {
		return bootstrap;
	}

	public NettyappenderServer(int port) {
		this.port = port;
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

	protected UnmarshallerProvider createProvider(MarshallerFactory factory, MarshallingConfiguration config) {
		return new DefaultUnmarshallerProvider(factory, config);

	}

	public void run() {
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newFixedThreadPool(4), Executors.newFixedThreadPool(4)));
		final ExecutionHandler executionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(5, 1024 * 1024 * 200, 1024 * 1024 * 200 * 2));
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		// bootstrap.setOption("writeBufferHighWaterMark", 100 * 64 * 1024);
		// bootstrap.setOption("sendBufferSize", 1048576);
		bootstrap.setOption("receiveBufferSize", 1048576 );

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(executionHandler, new MarshallingDecoder(createProvider(createMarshallerFactory(), createMarshallingConfig())),
						new NettyappenderServerHandler());
			}
		});
		LoggerFactory.getLogger(this.getClass()).info("start server at" + port);
		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(port));
	}

	public void shutdown() {
		if (bootstrap != null)
			bootstrap.shutdown();
	}

	public static void main(String[] args) throws Exception {
		int port = 4560;
		int timeout = 0;
		if (args.length > 0) {
			timeout = Integer.parseInt(args[0]);
		}

		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		configureLC(lc, NettyappenderServer.class.getResource("").getFile() + File.separator + "logbackserver.xml");

		NettyappenderServer nettyappenderServer = new NettyappenderServer(port);
		// success lines
		int successlines = 0;
		if (args.length > 1) {
			successlines = Integer.parseInt(args[0]);
		}

		nettyappenderServer.run();
		// if timeout >0 then autoshutdown after timeout,just for unit test
		if (timeout > 0) {
			TimeUnit.SECONDS.sleep(timeout);
			lc.getLogger(NettyappenderServer.class).debug("shut down");
			nettyappenderServer.shutdown();

			System.exit(0);
		}
	}

	static public void configureLC(LoggerContext lc, String configFile) throws JoranException {
		JoranConfigurator configurator = new JoranConfigurator();
		lc.reset();
		configurator.setContext(lc);
		configurator.doConfigure(configFile);
	}
}