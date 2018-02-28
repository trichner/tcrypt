package ch.n1b.tcrypt.cryptor;

/**
 * Generic Exception during encryption/decryption.
 *
 * @author Thomas Richner <mail@n1b.ch>
 * @created 2018-02-25
 *
 */
public class CryptoException extends Exception {
	public CryptoException() {
	}

	public CryptoException(String message) {
		super(message);
	}

	public CryptoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CryptoException(Throwable cause) {
		super(cause);
	}

	public CryptoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
