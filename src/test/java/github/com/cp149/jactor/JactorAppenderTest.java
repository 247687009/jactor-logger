package github.com.cp149.jactor;

import github.com.cp149.AppenderBaseTest;

import org.testng.annotations.Test;



/**
 * @author cp149
 *faster logappand by jactor but lost data because system exit too fast
 */
@Test(groups="normaltest")
public class JactorAppenderTest extends AppenderBaseTest

{

	
	@Test(invocationCount = 100, threadPoolSize = 30)
	public void testLog() {
		for (int i = 0; i < loglines; i++)
			logback.debug("logback I am faster {} at thread {}", i, Thread.currentThread().getId());

	}

}
