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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * @author netty test
 *create a netty server then run nettyappend class by mvn ,count how many lines the netty server received
 */
public class NettyTest {
	
	
	protected String testclass = "-Dtest=github.com.cp149.netty.client.NettyAppenderTest";
	protected String configFile = this.getClass().getResource("").getFile() + File.separator + "logbackserver.xml";
	protected String logfilename = "logs/logback-server-"+  new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log";
	protected final int experttotal = AppenderBaseTest.loglines*100+AppenderBaseTest.WARMLOGSIZE+1;
	private NettyappenderServer nettyappenderServer;

	@BeforeMethod(alwaysRun=true)
	public void befortest() throws Exception{
		CountAppender.count.set(0);
		File file=new File(logfilename);
		if(file.exists())file.delete();
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		NettyappenderServer.configureLC(lc, configFile);
		nettyappenderServer = new NettyappenderServer(4560);			
		nettyappenderServer.run();
	}
	@AfterMethod(alwaysRun=true)
	public void aftertest() throws Exception{
		nettyappenderServer.shutdown();
	}
	@Test(timeOut=40000,groups="nettytest")
	public void testNettyclientandserver() throws Exception{			
		new MvnCommandexe().executeCommands("mvn.bat",testclass,"test");
		int totallogs=CountAppender.count.intValue();
		
		while(totallogs<experttotal){
			totallogs=CountAppender.count.intValue();
			TimeUnit.SECONDS.sleep(2);
			System.out.println(this.getClass().getSimpleName()+"current lines ="+totallogs+" expert "+experttotal);
			
		}	
		System.out.println(Thread.currentThread().getStackTrace()[1]+"last total="+totallogs);
		Assert.assertEquals(CountAppender.count.intValue(), experttotal);
		Assert.assertEquals(Testutils.countlines(logfilename), experttotal);	
 
		
	}
	


}
