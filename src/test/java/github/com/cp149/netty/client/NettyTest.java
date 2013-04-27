package github.com.cp149.netty.client;

import github.com.cp149.AppenderBaseTest;
import github.com.cp149.CountAppender;
import github.com.cp149.MvnCommandexe;
import github.com.cp149.Testutils;
import github.com.cp149.netty.server.NettyappenderServer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import ch.qos.logback.classic.LoggerContext;

/**
 * @author netty test
 *create a netty server then run nettyappend class by mvn ,count how many lines the netty server received
 */
public class NettyTest {
	
	
	@Test
	public void testNettyclientandserver() throws Exception{
		String filename = "logs/logback-server-"+  new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log";
		File file=new File(filename);
		if(file.exists())file.delete();
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		NettyappenderServer.configureLC(lc, this.getClass().getResource("").getFile() + File.separator + "logbackserver.xml");
		NettyappenderServer nettyappenderServer = new NettyappenderServer(4560);			
		nettyappenderServer.run();
		
		new MvnCommandexe().executeCommands("mvn.bat","-Dtest=github.com.cp149.netty.client.NettyAppenderTest","test");
		int experttotal=AppenderBaseTest.loglines*100+AppenderBaseTest.WARMLOGSIZE+1;
		int totallogs=CountAppender.count.intValue();
		
		while(totallogs!=Testutils.countlines(filename) || totallogs<CountAppender.count.intValue()){
			totallogs=CountAppender.count.intValue();
			TimeUnit.SECONDS.sleep(4);
			System.out.println(totallogs);
			
		}
		TimeUnit.SECONDS.sleep(5);
		System.out.println("last total="+totallogs);
		Assert.assertEquals(CountAppender.count.intValue(), experttotal);
			
//		nettyappenderServer.getBootstrap().getPipeline().getChannel().isConnected(); 
		
	}
	


}