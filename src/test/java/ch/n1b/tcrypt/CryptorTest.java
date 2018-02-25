package ch.n1b.tcrypt;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

import ch.n1b.tcrypt.cryptor.AesGcmCryptor;
import ch.n1b.tcrypt.cryptor.BadMacTagException;
import ch.n1b.tcrypt.cryptor.BadVersionException;
import ch.n1b.tcrypt.cryptor.Cryptor;
import ch.n1b.tcrypt.cryptor.StringCryptor;
import ch.n1b.tcrypt.utils.HexStringUtils;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Thomas Richner <mail@n1b.ch>
 * @created 2018-02-25
 */
public class CryptorTest {

	@Test
	public void encrypt_bytes() throws Exception {
		AesGcmCryptor cryptor = new AesGcmCryptor();
		char[] password = "suchsecret,verywow".toCharArray();
		byte[] message = "I like turtles!".getBytes(StandardCharsets.UTF_8);

		byte[] ciphertext = cryptor.encrypt(password, message);

		System.out.println("Ciphertext:" + HexStringUtils.byteArrayToHexString(ciphertext));
		System.out.println("Plaintext Length:" + message.length);
		System.out.println("Ciphertext Length:" + ciphertext.length);

		byte[] plaintext = cryptor.decrypt(password, ciphertext);

		assertThat(plaintext, is(message));
	}

	@Test
	public void encrypt_str() throws Exception {

		StringCryptor cryptor = new Cryptor();
		char[] password = "ManySecret Much Wow".toCharArray();
		String message = "The answer to live, the universe and everything.";
		String ciphertext = cryptor.encrypt(password, message);

		String plaintext = cryptor.decrypt(password, ciphertext);

		assertThat(plaintext, is(message));
	}

	@Test
	public void encrypt_str_unicode_pw() throws Exception {

		StringCryptor cryptor = new Cryptor();
		char[] password = "hüntér12🚀".toCharArray();
		String message = "The answer to 🦄, the ✨ and everything.";
		String ciphertext = cryptor.encrypt(password, message);

		String plaintext = cryptor.decrypt(password, ciphertext);

		assertThat(plaintext, is(message));
	}

	@Test
	public void encrypt_str_1k() throws Exception {

		StringCryptor cryptor = new Cryptor();
		char[] password = "hunter12".toCharArray();

		final int ONE_THOUSAND = 1000;
		String message = randomString(ONE_THOUSAND, 1234L);
		assertThat(message.length(), is(ONE_THOUSAND));

		String ciphertext = cryptor.encrypt(password, message);

		String plaintext = cryptor.decrypt(password, ciphertext);

		assertThat(plaintext, is(message));
	}

	@Test(expected = BadMacTagException.class)
	public void encrypt_str_tampered_pw() throws Exception {

		StringCryptor cryptor = new Cryptor();
		char[] password = "ManySecret Much Wow".toCharArray();
		String message = "The answer to live, the universe and everything.";
		String ciphertext = cryptor.encrypt(password, message);

		password[password.length / 2] ^= 0x42;
		String plaintext = cryptor.decrypt(password, ciphertext);

		assertThat(plaintext, is(message));
	}

	@Test
	public void encrypt_str_tampered_ciphertext() throws Exception {
		// flipping any bit in the ciphertext should result in an exception
		// -> integrity

		StringCryptor cryptor = new Cryptor();
		char[] password = "ManySecret Much Wow".toCharArray();
		String message = "The answer.";
		String ciphertext = cryptor.encrypt(password, message);

		byte[] bytes = HexStringUtils.hexStringToByteArray(ciphertext);
		for (int i = 0; i < bytes.length * 8; i++) {
			// flip one bit
			byte[] touched = Arrays.copyOf(bytes, bytes.length);
			touched[i / 8] ^= (1 << i % 8);
			String touchedStr = HexStringUtils.byteArrayToHexString(touched);

			try {
				cryptor.decrypt(password, touchedStr);
				fail("Tampered ciphertext accepted!");
			} catch (BadVersionException | BadMacTagException e) {
				// success, integrity should be violated by flipping any bit
			}
		}

		String plaintext = cryptor.decrypt(password, ciphertext);
		assertThat("decryption works", plaintext, is(message));
	}

	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .?=-~!@#$%^&*()_:";

	private String randomString(int length, long seed) {
		Random r = new Random(seed);
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			int p = r.nextInt(ALPHABET.length());

			sb.append(ALPHABET.charAt(p));
		}

		return sb.toString();
	}
}