# Plain Java Encryption/Decryption

This library/CLI can encrypt and decrypt strings while
providing __confidentiality__ and __integrity__ of the plaintext. It shall be secure under a chosen plaintext attack.

This is achived by using 128bit AES encryption in the GCM blockmode.

## Build Instructions

1. get `maven`
2. run `mvn package`

## CLI Usage

1. build the binary (see [build instructions](#build-instructions))
2. run `java -jar tcrypt*.jar` to print usage (or `./tcrypt.sh`)

### Encryption

`java -jar tcrypt*.jar enc <property key> <plaintext filename>`

The ciphertext will be printed to stdout.

### Decryption

`java -jar tcrypt*.jar dec <property key> <ciphertext filename>`

The plaintext will be printed to stdout.