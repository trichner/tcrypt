package ch.n1b.tcrypt;

import ch.n1b.tcrypt.cryptor.AesGcmCryptor;
import org.apache.commons.codec.binary.Base64;

public final class AppDirectContent {

    private static final String UNICODE_FORMAT = "UTF8";

    private static final AesGcmCryptor CRYPTOR = new AesGcmCryptor();

    public static void main(String[] args) {

        if (args.length != 3) {
            printUsage();
            return;
        }

        final String mode = args[0];
        final String password = args[1];
        final String data = args[2];

        if ("encode".equals(mode)) {
            System.out.println(encrypt(password, data));
            return;
        }

        if ("decode".equals(mode)) {
            System.out.println(decrypt(password, data));
            return;
        }

        printUsage();
    }

    private static String encrypt(final String password, final String unencryptedString) {
        try {
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = CRYPTOR.encrypt(password.toCharArray(), plainText);
            return new String(Base64.encodeBase64(encryptedText));
        } catch (final Exception e) {
            throw new RuntimeException("Unable to encrypt!", e);
        }
    }

    private static String decrypt(final String password, final String data) {
        try {
            byte[] encryptedText = CRYPTOR.decrypt(password.toCharArray(),Base64.decodeBase64(data));
            return new String(encryptedText);
        } catch (final Exception e) {
            throw new RuntimeException("Unable to decrypt!", e);
        }
    }


    private static void printUsage() {
        System.out.printf("Usage: tcrypt ( encode | decode ) [master key / password] [data to encode/decode]%n");
    }
}
