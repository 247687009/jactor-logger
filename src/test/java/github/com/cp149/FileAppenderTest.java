package github.com.cp149;


import org.testng.annotations.Test;

/**
 * @author cp149
 * normal fileappender test,show how slow it run,but not lost data;
 */
@Test(groups="normaltest")
public class FileAppenderTest extends AppenderBaseTest

{
	

	
	public FileAppenderTest() {
		super();
		 LOGBACK_XML = "logback-filedemo.xml";
	}


	/**
	 * test logappand demo by file
	 */
	@Test(invocationCount = 100, threadPoolSize = 30)
	public void testLog() {
		for (int i = 0; i < loglines; i++)
			logback.debug("logback I am slow {}  at thread {}" ,i, Thread.currentThread().getId());

	}
}
