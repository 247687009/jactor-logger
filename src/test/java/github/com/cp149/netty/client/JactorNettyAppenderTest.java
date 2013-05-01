package github.com.cp149.netty.client;

import org.testng.annotations.Test;

import github.com.cp149.AppenderBaseTest;

public class JactorNettyAppenderTest extends AppenderBaseTest {
	@Test(invocationCount=100,threadPoolSize=30)
    public void testLog() throws Exception
    {
    	for (int i = 0; i < loglines; i++)
			logback.debug("logback test jactor and netty {} at thread {}" ,i, Thread.currentThread().getId());
    	
    }
	

	public JactorNettyAppenderTest() {
		super();
		LOGBACK_XML = "logback-netty.xml";
		Logfile = "logback-server-";
	}

}
