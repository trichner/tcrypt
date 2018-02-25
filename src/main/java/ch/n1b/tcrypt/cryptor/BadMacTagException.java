package ch.n1b.tcrypt.cryptor;

/**
 * Exception to indicate the MAC tag is bad.
 *
 * @author Thomas Richner <mail@n1b.ch>
 * @created 2018-02-25
 */
public class BadMacTagException extends Exception {
	public BadMacTagException() {
	}

	public BadMacTagException(String message) {
		super(message);
	}

	public BadMacTagException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadMacTagException(Throwable cause) {
		super(cause);
	}

	public BadMacTagException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
