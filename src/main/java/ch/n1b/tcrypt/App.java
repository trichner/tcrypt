package ch.n1b.tcrypt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;

import ch.n1b.tcrypt.cryptor.BadMacTagException;
import ch.n1b.tcrypt.cryptor.BadVersionException;
import ch.n1b.tcrypt.cryptor.CryptoException;
import ch.n1b.tcrypt.cryptor.properties.BadKeyException;
import ch.n1b.tcrypt.cryptor.properties.PropertyCryptor;
import ch.n1b.tcrypt.utils.IOUtils;

/**
 * CLI to encrypt/decrypt single properties with a password.
 *
 * @author Thomas Richner <mail@n1b.ch>
 * @created 2018-02-25
 */
public class App {

	public static void main(String[] args) {

		if (args.length == 0) {
			printUsage();
			return;
		}

		char[] password = readPassword();
		if (password == null) {
			panic("No password provided.");
		}

		//		System.err.println("PW: " + new String(password));

		if ("enc".equals(args[0])) {
			encrypt(args, password);
			return;
		}

		if ("dec".equals(args[0])) {
			decrypt(args, password);
			return;
		}

		// print usage

		printUsage();
	}

	private static void printEncUsage() {
		System.out.printf("Usage: tcrypt enc <property key> <filename>%n");
	}

	private static void encrypt(String[] args, char[] password) {

		if (args.length <= 2) {
			printEncUsage();
			System.exit(1);
		}

		String propertyKey = args[1];
		String filename = args[2];

		InputStream is = null;
		try {
			is = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			panic("File '%s' not found.%n", filename);
		}

		String plaintext = null;
		try {
			plaintext = IOUtils.toString(is);
		} catch (IOException e) {
			panic("Cannot read input: %s%n", e.getMessage());
		}

		assert plaintext != null;

		// encrypt
		PropertyCryptor cryptor = new PropertyCryptor();

		String ciphertext = null;
		try {
			ciphertext = cryptor.encrypt(password, propertyKey, plaintext);
		} catch (CryptoException | BadKeyException e) {
			panic("%s%n", e.getMessage());
		}

		assert ciphertext != null;

		System.out.println(ciphertext);
	}

	private static void printDecUsage() {
		System.out.printf("Usage: tcrypt dec <property key> <filename>%n");
	}

	private static void decrypt(String[] args, char[] password) {

		if (args.length <= 2) {
			printDecUsage();
			System.exit(1);
		}

		String propertyKey = args[1];
		String filename = args[2];

		InputStream is = System.in;
		try {
			is = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			panic("File '%s' not found.%n", filename);
		}

		String ciphertext = null;
		try {
			ciphertext = IOUtils.toString(is);
			if (ciphertext == null) {
				panic("Cannot read ciphertext.");
			}
			ciphertext = ciphertext.trim();
		} catch (IOException e) {
			panic("Cannot read input: %s%n", e.getMessage());
		}

		assert ciphertext != null;

		// encrypt
		PropertyCryptor cryptor = new PropertyCryptor();

		String plaintext = null;
		try {
			plaintext = cryptor.decrypt(password, propertyKey, ciphertext);
		} catch (CryptoException | BadKeyException | BadMacTagException | BadVersionException | ParseException e) {
			panic("%s%n", e.getMessage());
		}

		assert plaintext != null;

		System.out.println(plaintext);
	}

	private static char[] readPassword() {

		if (System.console() != null) {

			System.err.printf("Enter passphrase: ");
			char[] password1 = System.console().readPassword();

			System.err.printf("Confirm passphrase: ");
			char[] password2 = System.console().readPassword();

			if (!Arrays.equals(password1, password2)) {
				panic("Passphrase don't match.");
			}
			Arrays.fill(password2, ' ');
			return password1;
		}

		panic("No tty found, please provide the password as an argument.");
		return null;
	}

	private static void printUsage() {
		System.out.printf("Usage: tcrypt ( enc | dec ) [filename]%n");
	}

	private static void panic(String fmt, Object... args) {
		System.err.printf(fmt, args);
		System.exit(1);
	}
}
