package ch.n1b.tcrypt;

import java.text.ParseException;

import ch.n1b.tcrypt.utils.HexStringUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Thomas Richner <mail@n1b.ch>
 * @created 2018-02-25
 */
public class HexStringUtilsTest {

	@Test
	public void encode() throws ParseException {
		byte[] bytes = new byte[] { 1, 2, 3, 4, (byte) 128, (byte) 140, (byte) 255 };
		String expectEncoded = "01020304808CFF";

		String encoded = HexStringUtils.byteArrayToHexString(bytes);
		assertThat(expectEncoded, is(expectEncoded));

		byte[] decoded = HexStringUtils.hexStringToByteArray(encoded);

		assertThat(decoded, is(bytes));

		System.out.println(encoded);
	}

	@Test
	public void decode() throws ParseException {

		String encoded = "01020304808CFF";

		byte[] decoded = HexStringUtils.hexStringToByteArray(encoded);

		System.out.println(byteArrayToString(decoded));
	}

	@Test
	public void hex_digits() throws ParseException {

		char[] lowerChars = "0123456789abcdef".toCharArray();
		int i = 0;
		for (char c : lowerChars) {

			String s = Character.toString(c);
			int d = HexStringUtils.toHexDigit(s, 0);
			assertThat(d, is(i));
			i++;
		}

		char[] upperChars = "0123456789ABCDEF".toCharArray();
		int j = 0;
		for (char c : upperChars) {

			String s = Character.toString(c);
			int d = HexStringUtils.toHexDigit(s, 0);
			assertThat(d, is(j));
			j++;
		}

	}

	private String byteArrayToString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();

		for (byte b : bytes) {
			sb.append(String.format("0x%02X ", b));
		}
		return sb.toString();
	}
}
