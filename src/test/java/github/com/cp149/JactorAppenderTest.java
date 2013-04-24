package github.com.cp149;

import org.testng.annotations.Test;

public class JactorAppenderTest extends AppenderBaseTest

{

	/**
	 * faster logappand by jactor but lost data
	 */
	@Test(invocationCount = 100, threadPoolSize = 10)
	public void testLog() {
		for (int i = 0; i < loglines; i++)
			logback.debug("logback I am faster " + i + " at thread" + Thread.currentThread().getId());

	}

}
