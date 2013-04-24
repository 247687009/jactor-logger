package github.com.cp149.netty.client;

import github.com.cp149.AppenderBaseTest;

import java.io.File;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class NettyAppenderTest extends AppenderBaseTest{
		   
	@Test(invocationCount=100,threadPoolSize=100)
    public void testLog() throws Exception
    {
    	for (int i = 0; i < loglines; i++)
			logback.debug("logback test jasocket " + i + " at thread" + Thread.currentThread().getId());
    	
    }

	public NettyAppenderTest() {
		super();
		Logfile = "logback-server-";
	}
	

}
