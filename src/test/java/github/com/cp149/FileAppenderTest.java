package github.com.cp149;

import org.testng.annotations.Test;

public class FileAppenderTest extends AppenderBaseTest

{
	

	
	public FileAppenderTest() {
		super();
		 LOGBACK_XML = "logback-filedemo.xml";
	}


	/**
	 * test logappand demo by file
	 */
	@Test(invocationCount = 100, threadPoolSize = 10)
	public void testLog() {
		for (int i = 0; i < longlines; i++)
			logback.debug("logback I am slow " + i + " at thread" + Thread.currentThread().getId());

	}
}
