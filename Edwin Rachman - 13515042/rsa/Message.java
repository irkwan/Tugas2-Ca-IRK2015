package rsa;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

@SuppressWarnings("ALL")
public class Message {
  public byte[] content;

  private Message (byte[] content) {
    this.content = content;
  }

  public static Message fromString (String string, String charsetName) throws UnsupportedEncodingException {
    return new Message(string.getBytes(charsetName));
  }

  public String toString (String charsetName) throws UnsupportedEncodingException {
    return new String(content, charsetName);
  }

  public static Message fromHexString (String string) {
    return new Message(DatatypeConverter.parseHexBinary(string));
  }

  public String toHexString () {
    return DatatypeConverter.printHexBinary(content);
  }

  public static Message fromFile (File file) throws IOException {
    return new Message(Files.readAllBytes(file.toPath()));
  }

  public BigNumber toBigNumber () {
    return BigNumber.fromByteArray(content);
  }

  public static Message fromBigNumber (BigNumber bigNumber) {
    return new Message(bigNumber.toByteArray());
  }

  public void toFile (File file) throws IOException {
    Files.write(file.toPath(), content);
  }

  public static int getLengthLimit (String hashAlgorithm, int modulusByteCount) throws NoSuchAlgorithmException {
    return modulusByteCount - 2 * hashLength(hashAlgorithm) - 2;
  }

  public Message encode (String label, String labelCharsetName, String hashAlgorithm, int modulusByteCount) throws NoSuchAlgorithmException, IOException, RSAException {
    byte[] labelHash = hash(label.getBytes(labelCharsetName), hashAlgorithm);
    int hashLength = hashLength(hashAlgorithm);

    if (content.length > modulusByteCount - 2 * hashLength - 2) {
      throw new RSAException("Message length too long");
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.write(labelHash);
    for (int i = 0; i < modulusByteCount - content.length -  2 * hashLength - 2; ++i) {
      outputStream.write(0x00);
    }
    outputStream.write(0x01);
    outputStream.write(content);

    byte[] dataBlock = outputStream.toByteArray();

    Random random = new SecureRandom();
    byte[] seed = new byte[hashLength];
    random.nextBytes(seed);

    byte[] dataBlockMask = maskGenerationFunction(seed, dataBlock.length, "SHA-1");
    for (int i = 0; i < dataBlock.length; ++i) {
      dataBlock[i] ^= dataBlockMask[i];
    }

    byte[] seedMask = maskGenerationFunction(dataBlock, seed.length, "SHA-1");
    for (int i = 0; i < seed.length; ++i) {
      seed[i] ^= seedMask[i];
    }

    outputStream.reset();
    outputStream.write(0);
    outputStream.write(seed);
    outputStream.write(dataBlock);

    return new Message(outputStream.toByteArray());
  }

  public Message decode (String label, String labelCharsetName, String hashAlgorithm, int modulusByteCount) throws NoSuchAlgorithmException, IOException, RSAException {
    byte[] labelHash = hash(label.getBytes(labelCharsetName), hashAlgorithm);
    int hashLength = hashLength(hashAlgorithm);

    if (content.length != modulusByteCount || modulusByteCount < 2 * hashLength + 2) {
      throw new RSAException("Decryption error");
    }

    byte headByte = content[0];
    byte[] seed = Arrays.copyOfRange(content, 1, hashLength + 1);
    byte[] dataBlock = Arrays.copyOfRange(content, hashLength + 1, modulusByteCount);

    byte[] seedMask = maskGenerationFunction(dataBlock, seed.length, "SHA-1");
    for (int i = 0; i < seed.length; ++i) {
      seed[i] ^= seedMask[i];
    }

    byte[] dataBlockMask = maskGenerationFunction(seed, dataBlock.length, "SHA-1");
    for (int i = 0; i < dataBlock.length; ++i) {
      dataBlock[i] ^= dataBlockMask[i];
    }

    int i = hashLength;
    while (i < dataBlock.length && dataBlock[i] != 0x01) {
      ++i;
    }

    if (i == dataBlock.length || !Arrays.equals(Arrays.copyOfRange(dataBlock, 0, hashLength), labelHash) || headByte != 0x00) {
      throw new RSAException("Decryption error");
    }
    else {
      return new Message(Arrays.copyOfRange(dataBlock, i + 1, dataBlock.length));
    }
  }

  private static byte[] hash (byte[] message, String hashAlgorithm) throws NoSuchAlgorithmException, IOException {
    return MessageDigest.getInstance(hashAlgorithm).digest(message);
  }

  private static int hashLength (String hashAlgorithm) throws NoSuchAlgorithmException {
    return MessageDigest.getInstance(hashAlgorithm).getDigestLength();
  }

  private static byte[] maskGenerationFunction (byte[] seed, int length, String hashAlgorithm) throws NoSuchAlgorithmException, IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    int i = 0;
    while (outputStream.size() < length) {
      outputStream.write(hash(ByteBuffer.allocate(seed.length + 4).put(seed).putInt(i).array(), hashAlgorithm));
      ++i;
    }
    return Arrays.copyOfRange(outputStream.toByteArray(), 0, length);
  }
}
