package ch.n1b.tcrypt.utils;

import java.text.ParseException;

/**
 * Utility to convert from hex strings to bytes and vice-versa
 *
 * @author Thomas Richner <mail@n1b.ch>
 * @created 2018-02-25
 */
public class HexStringUtils {

	public static byte[] hexStringToByteArray(String s) throws ParseException {

		int len = s.length();
		if ((len % 2) != 0) {
			throw new ParseException(s, 0);
		}

		byte[] data = new byte[len / 2];
		for (int i = 0; i < len - 1; i += 2) {
			byte b = (byte) toHexDigit(s, i);
			b <<= 4;
			b |= toHexDigit(s, i + 1);
			data[i / 2] = b;
		}
		return data;
	}

	public static int toHexDigit(String s, int pos) throws ParseException {
		int d = Character.digit(s.charAt(pos), 16);
		if (d < 0) {
			throw new ParseException(s, pos);
		}
		return d;
	}

	public static String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
}
