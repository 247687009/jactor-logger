package github.com.cp149;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class AppenderBaseTest {
	public static final int WARMLOGSIZE = 500;
	// logback config file
	protected String LOGBACK_XML = "logback.xml";
	// log out put file
	protected String Logfile = "logback-";
	// file size of logfile before test
	protected int sizeBeforTest = 0;

	protected org.slf4j.Logger logback = LoggerFactory.getLogger(this.getClass());
	private Logger debuglog = LoggerFactory.getLogger("test.info");
	private LoggerContext lc;

	// test start time ,
	protected long starttime;
	// actual full logfile name
	private String filename;

	// log per thread
	public static int loglines = 10000;

	public AppenderBaseTest() {
		super();
	}

	@BeforeClass(alwaysRun = true, timeOut = 20000)
	public void initLogconfig() throws Exception {
		filename = "logs/" + Logfile + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log";
		File file = new File(filename);
		if (file.exists()) {
			sizeBeforTest = Testutils.countlines(filename);
		}		
		configureLC();
		CountAppender.count.set(0);
		debuglog.debug(file.getAbsolutePath());
		// warmup the logfile
		for (int i = 0; i < WARMLOGSIZE; i++)
			logback.debug("warm logsystem");
		// while(CountAppender.count.intValue()!=
		// WARMLOGSIZE)TimeUnit.MILLISECONDS.sleep(500);
		starttime = System.nanoTime();

	}

	private void configureLC() throws JoranException {
		lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		String configFile = this.getClass().getResource("").getFile() + File.separator + LOGBACK_XML;
		JoranConfigurator configurator = new JoranConfigurator();
		lc.reset();
		configurator.setContext(lc);
		configurator.doConfigure(configFile);
	}

	@AfterClass(timeOut = 20000, alwaysRun = true)
	public void afteclass() throws Exception {
		// get total test run time
		long runtime = System.nanoTime() - starttime;
		debuglog.debug(this.getClass().getSimpleName() + " thread run over time=" + runtime);
		int expectlines = 100 * loglines + WARMLOGSIZE;
		// get total log

		while (CountAppender.count.intValue() < expectlines) {
			TimeUnit.MILLISECONDS.sleep(300);
			debuglog.debug(this.getClass().getSimpleName() + "current lines time=" + CountAppender.count);
		}
		// get the write time
		debuglog.debug(this.getClass().getSimpleName() + " total  time=" + (System.nanoTime() - starttime)/1000000+ " total lines="
				+ expectlines);

		int fileline = Testutils.countlines(filename) - sizeBeforTest;
		Assert.assertEquals(fileline, expectlines);

	}
}