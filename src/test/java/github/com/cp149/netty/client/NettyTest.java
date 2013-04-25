package github.com.cp149.netty.client;

import github.com.cp149.MvnCommandexe;
import github.com.cp149.netty.server.NettyappenderServer;

import java.io.File;

import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import ch.qos.logback.classic.LoggerContext;

public class NettyTest {
	
	@Test
	public void testNettyclientandserver() throws Exception{
//		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//		NettyappenderServer.configureLC(lc, NettyappenderServer.class.getResource("").getFile() + File.separator + "logbackserver.xml");
//		NettyappenderServer nettyappenderServer = new NettyappenderServer(4560);			
//		nettyappenderServer.run();
		new MvnCommandexe().executeCommands("mvn.bat","-Dtest=github.com.cp149.netty.client.NettyAppenderTest","test");
//		nettyappenderServer.getBootstrap().getPipeline().getChannel().isConnected(); 
		
	}
	


}
