package github.com.cp149.disruptor;

import github.com.cp149.AppenderBaseTest;

import org.testng.annotations.Test;

@Test(groups = "normaltest")
public class DisruptorTest extends AppenderBaseTest {

	@Test(invocationCount = 100, threadPoolSize = 30)
	public void testLog() {
		for (int i = 0; i < loglines; i++)
			logback.debug("logback disruptor {} at thread {}", i, Thread.currentThread().getId());

	}

	public DisruptorTest() {
		super();
		this.Logfile = "logback-disruptor-";
	}

}
