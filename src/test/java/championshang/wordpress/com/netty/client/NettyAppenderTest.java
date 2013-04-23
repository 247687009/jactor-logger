package championshang.wordpress.com.netty.client;

import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import ch.qos.logback.classic.LoggerContext;

public class NettyAppenderTest {
	org.slf4j.Logger logback = LoggerFactory.getLogger(this.getClass());
	   
	@Test(invocationCount=100,threadPoolSize=100)
    public void testLog() throws Exception
    {
    	for (int i = 0; i < 1000; i++)
			logback.debug("logback test jasocket " + i + " at thread" + Thread.currentThread().getId());
    	
    }
	@AfterClass
	public void afterTest(){
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		lc.stop();
	}

}
