package github.com.cp149.netty.client;

import java.io.File;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class NettyAppenderTest {
	org.slf4j.Logger logback = LoggerFactory.getLogger(this.getClass());
	@BeforeClass
	public void initLogconfig() throws Exception{
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		configureLC(lc, this.getClass().getResource("").getFile() + File.separator + "logback.xml");
	}
	private void configureLC(LoggerContext lc, String configFile) throws JoranException {
		JoranConfigurator configurator = new JoranConfigurator();
		lc.reset();
		configurator.setContext(lc);
		configurator.doConfigure(configFile);
	}
	   
	@Test(invocationCount=100,threadPoolSize=100)
    public void testLog() throws Exception
    {
    	for (int i = 0; i < 1000; i++)
			logback.debug("logback test jasocket " + i + " at thread" + Thread.currentThread().getId());
    	
    }
	@AfterClass
	public void afterTest(){
//		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();		
//		lc.stop();
	}

}
