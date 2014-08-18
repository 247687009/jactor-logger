package github.com.cp149;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Testutils {

	/**
	 * @param filename
	 * @return
	 * @throws IOException
	 *             count how many lines a file
	 */
	public static int countlines(String filename) throws IOException {
		LineNumberReader lnr = new LineNumberReader(new FileReader(new File(filename)));
		try {
			lnr.skip(Long.MAX_VALUE);
		} catch (Exception e) {
			
		}finally{
		lnr.close();
		}
		return (lnr.getLineNumber());
	}

}
