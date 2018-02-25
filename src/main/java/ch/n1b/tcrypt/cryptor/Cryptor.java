package ch.n1b.tcrypt.cryptor;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import ch.n1b.tcrypt.utils.HexStringUtils;

/**
 * @author Thomas Richner <mail@n1b.ch>
 * @created 2018-02-25
 *
 * https://stackoverflow.com/questions/31851612/java-aes-gcm-nopadding-what-is-cipher-getiv-giving-me
 */
public class Cryptor implements StringCryptor {

	private final AesGcmCryptor aesGcmCryptor = new AesGcmCryptor();

	@Override
	public String encrypt(char[] password, String plaintext) throws CryptoException {

		byte[] encrypted = null;
		try {
			encrypted = aesGcmCryptor.encrypt(password, plaintext.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException //
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException //
				| InvalidKeySpecException e) {
			throw new CryptoException(e);
		}
		return HexStringUtils.byteArrayToHexString(encrypted);
	}

	@Override
	public String decrypt(char[] password, String ciphertext)
			throws CryptoException, BadMacTagException, BadVersionException, ParseException {

		byte[] ct = HexStringUtils.hexStringToByteArray(ciphertext);
		byte[] plaintext = null;
		try {
			plaintext = aesGcmCryptor.decrypt(password, ct);
		} catch (AEADBadTagException e) {
			throw new BadMacTagException(e);
		} catch ( //
				NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException //
						| InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException //
						| BadPaddingException e) {
			throw new CryptoException(e);
		}
		return new String(plaintext, StandardCharsets.UTF_8);
	}

}
