package telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author dream
 */

public class Pipe extends Thread {

	private final BufferedReader InS;
	private final PrintStream OutS;

	public Pipe(InputStream InS, OutputStream OutS) throws UnsupportedEncodingException {
		// big5 解決中文問題
		this.InS = new BufferedReader(new InputStreamReader(InS, "big5"));
		this.OutS = new PrintStream(OutS);
	}

	@Override
	public void run() {
		String line;
		try {
			while ((line = InS.readLine()) != null) {
				OutS.println(line);
				OutS.flush();
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage());
		}

	}
}

