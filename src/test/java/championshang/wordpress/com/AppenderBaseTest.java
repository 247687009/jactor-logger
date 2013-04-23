package championshang.wordpress.com;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class AppenderBaseTest {
	protected String LOGBACK_XML = "logback.xml";
	protected org.slf4j.Logger logback = LoggerFactory.getLogger(this.getClass());
	private LoggerContext lc;
	private File file;

	public AppenderBaseTest() {
		super();
	}

	@BeforeClass(alwaysRun = true)
	public void initLogconfig() throws Exception {
		file = new File("logs/logback-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log");
		if (file.exists())
			file.delete();
		System.out.println(file.getAbsolutePath());
		lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		System.out.println("init config" + LOGBACK_XML);
		configureLC(lc, this.getClass().getResource("").getFile() + File.separator + LOGBACK_XML);
	}

	private void configureLC(LoggerContext lc, String configFile) throws JoranException {
		JoranConfigurator configurator = new JoranConfigurator();
		lc.reset();
		configurator.setContext(lc);
		configurator.doConfigure(configFile);
	}

	@AfterClass()
	public void afteclass() throws IOException {
		file = new File("logs/logback-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log");
		Assert.assertEquals(FileUtils.readLines(file).size(), 100 * 1000);		
	}

}