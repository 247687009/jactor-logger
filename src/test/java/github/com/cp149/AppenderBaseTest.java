package github.com.cp149;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;
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
	private StopWatch stopWatch;
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
		logback.debug("init config" + LOGBACK_XML);
		stopWatch = new LoggingStopWatch();

	}

	private void configureLC(LoggerContext lc, String configFile) throws JoranException {
		JoranConfigurator configurator = new JoranConfigurator();
		lc.reset();
		configurator.setContext(lc);
		configurator.doConfigure(configFile);
	}

	@AfterClass()
	public void afteclass() throws IOException {
		stopWatch.stop("run success", "Sleep time was " + stopWatch.getElapsedTime());

		file = new File("logs/logback-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log");
		Assert.assertEquals(countlines(filename)-sizeBeforTest, 100 * longlines + 1);

	}

	public int countlines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}
}