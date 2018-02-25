package ch.n1b.tcrypt.cryptor.properties;

import java.text.ParseException;
import java.util.regex.Pattern;

import ch.n1b.tcrypt.cryptor.BadMacTagException;
import ch.n1b.tcrypt.cryptor.BadVersionException;
import ch.n1b.tcrypt.cryptor.CryptoException;
import ch.n1b.tcrypt.cryptor.Cryptor;
import ch.n1b.tcrypt.cryptor.StringCryptor;

/**
 * @author Thomas Richner <mail@n1b.ch>
 * @created 2018-02-25
 */
public class PropertyCryptor {

	private static final String SEPARATOR = ":";
	private static final Pattern VALID_KEY = Pattern.compile("[-_.A-Za-z0-9]+");

	private final StringCryptor cryptor = new Cryptor();

	public String encrypt(char[] password, String key, String value) throws CryptoException, BadKeyException {

		validateKey(key);
		String msg = key + ':' + value;
		return cryptor.encrypt(password, msg);
	}

	public String decrypt(char[] password, String key, String ciphertext)
			throws BadMacTagException, BadVersionException, CryptoException, BadKeyException, ParseException {

		String msg = cryptor.decrypt(password, ciphertext);
		String[] splits = msg.split(SEPARATOR, 2);
		if (splits.length < 2) {
			throw new BadKeyException("Missing '" + SEPARATOR + "'.");
		}

		String parsedKey = splits[0];
		validateKey(parsedKey);
		if (!key.equals(parsedKey)) {
			throw new BadKeyException("Keys don't match: " + key + "!=" + parsedKey);
		}

		return splits[1];
	}

	private void validateKey(String key) throws BadKeyException {
		if (VALID_KEY.matcher(key).matches()) {
			return;
		}

		throw new BadKeyException("Bad key: " + key);
	}

}
