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

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import org.jboss.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import org.jboss.netty.handler.codec.marshalling.MarshallerProvider;
import org.jboss.netty.handler.codec.marshalling.MarshallingDecoder;
import org.jboss.netty.handler.codec.marshalling.UnmarshallerProvider;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.kryoDecoder;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * Echoes back any received data from a client.
 */
public class NettyappenderServer {

	private final int port;

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
		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new MarshallingDecoder(createProvider(createMarshallerFactory(), createMarshallingConfig())), new NettyappenderServerHandler());
			}
		});
		LoggerFactory.getLogger(this.getClass()).info("start server at" + port);
		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(port));
	}

	public static void main(String[] args) throws Exception {
		int port = 4560;
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		configureLC(lc, NettyappenderServer.class.getResource("").getFile() + File.separator + "logbackserver.xml");

		new NettyappenderServer(port).run();
	}

	static public void configureLC(LoggerContext lc, String configFile) throws JoranException {
		JoranConfigurator configurator = new JoranConfigurator();
		lc.reset();
		configurator.setContext(lc);
		configurator.doConfigure(configFile);
	}
}