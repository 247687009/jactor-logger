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
	
	
	protected String testclass = "-Dtest=github.com.cp149.netty.client.NettyAppenderTest";
	protected String configFile = this.getClass().getResource("").getFile() + File.separator + "logbackserver.xml";
	protected String logfilename = "logs/logback-server-"+  new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log";
	protected final int experttotal = AppenderBaseTest.loglines*100+AppenderBaseTest.WARMLOGSIZE+1;

	@Test(timeOut=30000)
	public void testNettyclientandserver() throws Exception{
		File file=new File(logfilename);
		if(file.exists())file.delete();
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		NettyappenderServer.configureLC(lc, configFile);
		NettyappenderServer nettyappenderServer = new NettyappenderServer(4560);			
		nettyappenderServer.run();
		
		new MvnCommandexe().executeCommands("mvn.bat",testclass,"test");
		int totallogs=CountAppender.count.intValue();
		
		while(totallogs<experttotal){
			totallogs=CountAppender.count.intValue();
			TimeUnit.SECONDS.sleep(2);
			System.out.println(Thread.currentThread().getStackTrace()[1]+"current lines ="+totallogs);
			
		}	
		System.out.println(Thread.currentThread().getStackTrace()[1]+"last total="+totallogs);
		Assert.assertEquals(CountAppender.count.intValue(), experttotal);
		Assert.assertEquals(Testutils.countlines(logfilename), experttotal);	
 
		
	}
	


}
