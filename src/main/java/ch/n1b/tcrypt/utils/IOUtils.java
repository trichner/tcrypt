package ch.n1b.tcrypt.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility to handle input/output.
 *
 * @author Thomas Richner <mail@n1b.ch>
 * @created 2018-02-25
 */
public class IOUtils {

	/**
	 * Utility to read all bytes from an {@link java.io.InputStream} and decode it as UTF-8.
	 *
	 * @throws IOException
	 */
	public static String toString(InputStream is) throws IOException {

		ByteArrayOutputStream result = new ByteArrayOutputStream();
		final byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		return result.toString(StandardCharsets.UTF_8.name());
	}
}
