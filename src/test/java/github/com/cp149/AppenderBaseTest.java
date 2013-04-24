package github.com.cp149;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
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
	protected String LOGBACK_XML = "logback.xml";
	protected String Logfile = "logback-";
	protected org.slf4j.Logger logback = LoggerFactory.getLogger(this.getClass());
	private LoggerContext lc;
	private File file;
	private long starttime;
	private int sizeBeforTest = 0;
	private String filename;
	
	protected  int longlines=2000;

	public AppenderBaseTest() {
		super();
	}

	@BeforeClass(alwaysRun = true)
	public void initLogconfig() throws Exception {
		filename = "logs/" + Logfile + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log";
		file = new File(filename);
		if (file.exists()) {
			sizeBeforTest = countlines(filename);
		}
		System.out.println(file.getAbsolutePath());
		lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		configureLC(lc, this.getClass().getResource("").getFile() + File.separator + LOGBACK_XML);
		//test log is ok
		logback.debug("init config" + LOGBACK_XML);
		TimeUnit.SECONDS.sleep(1);
		starttime =new Date().getTime();

	}

	private void configureLC(LoggerContext lc, String configFile) throws JoranException {
		JoranConfigurator configurator = new JoranConfigurator();
		lc.reset();
		configurator.setContext(lc);
		configurator.doConfigure(configFile);
	}

	@AfterClass()
	public void afteclass() throws IOException {
		System.out.println("run times"+(new Date().getTime() -starttime));

		file = new File("logs/logback-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log");
		Assert.assertEquals(countlines(filename)-sizeBeforTest, 100 * longlines + 1);

	}

	public int countlines(String filename) throws IOException {
		LineNumberReader  lnr = new LineNumberReader(new FileReader(new File(filename)));
		lnr.skip(Long.MAX_VALUE);
		lnr.close();
		return(lnr.getLineNumber());
	}
}