package ch.n1b.tcrypt.cryptor;

import java.text.ParseException;

/**
 * Facade for encrypting and decrypting strings with a password.
 *
 * @author Thomas Richner <mail@n1b.ch>
 * @created 2018-02-25
 */
public interface StringCryptor {
	/**
	 * Encrypts a plaintext into a hex encoded string.
	 * It provides confidentiality and integrity of the plaintext.
	 *
	 * @throws CryptoException wrapped JCE exception
	 */
	String encrypt(char[] password, String plaintext) throws CryptoException;

	/**
	 * Decrypts a hex encoded ciphertext and must
	 * be the reverse operation of {@link StringCryptor#encrypt(char[], java.lang.String)}
	 * for any given implementation of {@link StringCryptor}
	 *
	 * @throws CryptoException     wrapped JCE exception
	 * @throws BadMacTagException  bad integrity tag (MAC)
	 * @throws BadVersionException invalid version for this cryptor
	 */
	String decrypt(char[] password, String ciphertext)
			throws CryptoException, BadVersionException, BadMacTagException, ParseException;
}
