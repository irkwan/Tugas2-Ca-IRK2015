![RSAEncryptor: Java-based RSA file encryption application][cover]

This application implements RSA key encryption on Java.

Note that this application does not rely entirely on RSA. The actual file encryption process is done in AES-128, while RSA-1024 is used to encrypt the AES encryption key. This is done as RSA is inefficient for files larger than 1024 bytes.

This application uses a homebrew `BigInt` library instead of the bundled `java.lang.BigInteger` library. The `BigInt` library is an attempt on implementing a BigInteger library and, while is mostly the same on several lower-level operations like bit shifts, implements other algorithms on higher-level operations such as division and prime generation.

## Quick Start
Download [RSAEncryptor.jar][jar] and run it.

## Application Features

### Key Management

![Key panel][s001]

Before encrypting and decrypting files, an RSA key pair must be either generated or loaded.

![Key generation][s002]

To generate keys, press the **Generate Keys** button. The application cannot be interacted with while the keys are generated. After key generation is done, there will be a prompt to save the generated key pair. A notice will pop up informing the time taken to generate the keys.

Generated keys will be automatically loaded and the application is ready to be used. The keys can be saved again if desired through the **Save Public Key** and **Save Private Key** buttons.

![Key pair loading][s003]

Instead of generating a new pair of keys every time the application is used, an existing pair of keys can be loaded through their respective buttons. If the keys are admissible, they will be loaded and shown on the text areas. Private keys can be used as public keys, but not vice-versa.

### Encryption

![Encryption panel][s004]

The encryption process uses a public key. If no public key has been set, an error will occur.

To encrypt, either type something in the text area or load a file through the **Browse** button. All opened files will be treated as UTF-8 texts.

![Encryption process][s005]

After encryption is done, a notice will pop up informing the time taken to encrypt the text. The encrypted text will be shown in the right text area. Be sure to save both the encrypted text and the manifest, as the manifest will be used in decrypting the encrypted text.

### Decryption

![Decryption panel][s006]

The encryption process uses a private key. If no private key has been set, an error will occur.

To decrypt, load the manifest file through the provided button. The encrypted text can either be loaded or pasted into the left text area.

![Decryption process][s007]

After decryption is done, a notice will pop up informing the time taken to decrypt the text. The decrypted text will be shown in the right text area.

## Additional Information

[Project specification](https://github.com/felixlimanta/Tugas2-Ca-IRK2015/blob/master/README.md)

[jar]: https://github.com/felixlimanta/Tugas2-Ca-IRK2015/raw/master/Felix%20Limanta%20-%2013515065/RSAEncryptor/RSAEncryptor.jar
[cover]: docs/readme_resources/cover.jpg
[s001]: docs/readme_resources/s001.jpg
[s002]: docs/readme_resources/s002.jpg
[s003]: docs/readme_resources/s003.jpg
[s004]: docs/readme_resources/s004.jpg
[s005]: docs/readme_resources/s005.jpg
[s006]: docs/readme_resources/s006.jpg
[s007]: docs/readme_resources/s007.jpg
