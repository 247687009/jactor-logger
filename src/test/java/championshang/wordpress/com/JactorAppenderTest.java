package championshang.wordpress.com;

import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;


public class JactorAppenderTest 
    
{
   
	org.slf4j.Logger logback = LoggerFactory.getLogger(this.getClass());
   
	@Test(invocationCount=100,threadPoolSize=10)
    public void testLog()
    {
    	for (int i = 0; i < 1000; i++)
			logback.info("logback I am ok " + i + " at thread" + Thread.currentThread().getId());
    }
}
