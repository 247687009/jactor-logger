package github.com.cp149.netty.client;

import github.com.cp149.AppenderBaseTest;

import org.testng.annotations.Test;

/**
 * @author cp149
 *nettyappender test ,call by nettytest
 */
public class NettyAppenderTest extends AppenderBaseTest{
		   
	@Test(invocationCount=100,threadPoolSize=30)
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
