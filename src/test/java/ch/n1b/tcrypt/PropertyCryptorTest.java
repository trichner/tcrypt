package ch.n1b.tcrypt;

import ch.n1b.tcrypt.cryptor.BadMacTagException;
import ch.n1b.tcrypt.cryptor.properties.BadKeyException;
import ch.n1b.tcrypt.cryptor.properties.PropertyCryptor;
import ch.n1b.tcrypt.utils.HexStringUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Thomas Richner <mail@n1b.ch>
 * @created 2018-02-25
 */
public class PropertyCryptorTest {

	@Test
	public void crypt_success() throws Exception {
		final char[] password = "hunter12".toCharArray();
		final String key = "apikey.zwitscher";
		final String value = "kd(28e9df(!)JF(JF::092jf";

		PropertyCryptor cryptor = new PropertyCryptor();

		String ciphertext = cryptor.encrypt(password, key, value);

		String plainvalue = cryptor.decrypt(password, key, ciphertext);

		assertThat("cryptor can decrypt", plainvalue, is(value));
	}

	@Test(expected = BadKeyException.class)
	public void crypt_bad_key() throws Exception {
		final char[] password = "hunter12".toCharArray();
		final String key = "apikey/zwitscher";
		final String value = "kd(28e9df(!)JF(JF::092jf";

		PropertyCryptor cryptor = new PropertyCryptor();
		cryptor.encrypt(password, key, value);
	}

	@Test(expected = BadKeyException.class)
	public void crypt_keys_no_match() throws Exception {
		final char[] password = "hunter12".toCharArray();
		final String key1 = "apikey.zwitscher";
		final String key2 = "apikey.zwatscher";
		final String value = "kd(28e9df(!)JF(JF::092jf";

		PropertyCryptor cryptor = new PropertyCryptor();

		String ciphertext = cryptor.encrypt(password, key1, value);

		cryptor.decrypt(password, key2, ciphertext);
	}

	@Test(expected = BadMacTagException.class)
	public void crypt_tampered_ciphertext() throws Exception {
		final char[] password = "hunter12".toCharArray();
		final String key = "apikey.zwitscher";
		final String value = "kd(28e9df(!)JF(JF::092jf";

		PropertyCryptor cryptor = new PropertyCryptor();

		String ciphertext = cryptor.encrypt(password, key, value);

		byte[] cipherbytes = HexStringUtils.hexStringToByteArray(ciphertext);
		cipherbytes[7] ^= 0x42;
		ciphertext = HexStringUtils.byteArrayToHexString(cipherbytes);

		cryptor.decrypt(password, key, ciphertext);
	}

}