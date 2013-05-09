package github.com.cp149;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class AppenderBaseTest {
	public static final int WARMLOGSIZE = 100;
	// logback config file
	protected String LOGBACK_XML = "logback.xml";
	// log out put file
	protected String Logfile = "logback-";
	// file size of logfile before test
	protected int sizeBeforTest = 0;

	protected org.slf4j.Logger logback = LoggerFactory.getLogger(this.getClass());
	private LoggerContext lc;

	// test start time ,
	protected long starttime;
	// actual full logfile name
	private String filename;

	// log per thread
	public static int loglines = 5000;

	public AppenderBaseTest() {
		super();
	}

	@BeforeClass(alwaysRun = true)
	public void initLogconfig() throws Exception {
		filename = "logs/" + Logfile + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log";
		File file = new File(filename);
		if (file.exists()) {
			sizeBeforTest = Testutils.countlines(filename);
		}
		System.out.println(file.getAbsolutePath());
		lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		configureLC(lc, this.getClass().getResource("").getFile() + File.separator + LOGBACK_XML);

		//warmup the logfile
		for(int i=0;i<WARMLOGSIZE;i++)
			logback.debug("warm logsystem");
		starttime = System.currentTimeMillis();

	}

	private void configureLC(LoggerContext lc, String configFile) throws JoranException {
		JoranConfigurator configurator = new JoranConfigurator();
		lc.reset();
		configurator.setContext(lc);
		configurator.doConfigure(configFile);
	}

	@AfterClass(timeOut=20000)
	public void afteclass() throws Exception {		
		//get total test run time
		long runtime = System.currentTimeMillis() - starttime;
		System.out.println( this.getClass().getSimpleName()+" thread run over time=" + runtime);
		//get total log 
		int expertlines = 100 * loglines + WARMLOGSIZE;
		while(CountAppender.count.intValue()<expertlines){
			TimeUnit.MILLISECONDS.sleep(300);
			System.out.println( this.getClass().getSimpleName()+"current lines time="  + CountAppender.count);	
		}
		//get the write time
		System.out.println( this.getClass().getSimpleName()+" total  time=" + (System.currentTimeMillis() - starttime));
		int fileline = Testutils.countlines(filename) -sizeBeforTest;
		Assert.assertEquals(fileline, expertlines);
//		while(fileline<expertlines){
//			TimeUnit.MILLISECONDS.sleep(500);
//			System.out.println( this.getClass().getSimpleName()+"current logfile lines time="  + fileline);
//			fileline = Testutils.countlines(filename) -sizeBeforTest;
//		}
		

	}
}