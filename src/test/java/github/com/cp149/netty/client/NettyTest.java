package github.com.cp149.netty.client;

import github.com.cp149.CountAppender;
import github.com.cp149.MvnCommandexe;
import github.com.cp149.netty.server.NettyappenderServer;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import ch.qos.logback.classic.LoggerContext;

public class NettyTest {
	
	
	@Test
	public void testNettyclientandserver() throws Exception{
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		NettyappenderServer.configureLC(lc, this.getClass().getResource("").getFile() + File.separator + "logbackserver.xml");
		NettyappenderServer nettyappenderServer = new NettyappenderServer(4560);			
		nettyappenderServer.run();
		new MvnCommandexe().executeCommands("mvn.bat","-Dtest=github.com.cp149.netty.client.NettyAppenderTest","test");
		int experttotal=2000*100+1;
		int totallogs=CountAppender.count.intValue();
		while(totallogs<CountAppender.count.intValue()){
			totallogs=CountAppender.count.intValue();
			TimeUnit.SECONDS.sleep(4);
			System.out.println(totallogs);			
		}
		System.out.println("last total="+totallogs);
		Assert.assertEquals(totallogs, experttotal);
			
//		nettyappenderServer.getBootstrap().getPipeline().getChannel().isConnected(); 
		
	}
	


}
