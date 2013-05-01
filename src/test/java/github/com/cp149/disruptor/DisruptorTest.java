package github.com.cp149.disruptor;

import org.testng.annotations.Test;

import github.com.cp149.AppenderBaseTest;
@Test(groups="normaltest")
public class DisruptorTest extends AppenderBaseTest {
	
	@Test(invocationCount = 100, threadPoolSize = 30)
	public void testLog() {
		for (int i = 0; i < loglines; i++)
			logback.debug("logback disruptor {} at thread {}", i, Thread.currentThread().getId());

	}

}
