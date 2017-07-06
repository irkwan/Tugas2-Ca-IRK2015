package org.felixlimanta.RSAEncryptor.controller;

import java.io.IOException;
import org.felixlimanta.RSAEncryptor.model.PrivateKey;
import org.felixlimanta.RSAEncryptor.model.PublicKey;
import org.felixlimanta.RSAEncryptor.view.DecryptPanel;
import org.felixlimanta.RSAEncryptor.view.EncryptPanel;
import org.felixlimanta.RSAEncryptor.view.KeyPanel;
import org.xml.sax.SAXException;

/**
 * Created by ASUS on 13/06/17.
 */
public class RsaEncryptorController {
  private RSA rsa;

  private EncryptPanel encryptPanel;
  private DecryptPanel decryptPanel;
  private KeyPanel keyPanel;

  public RsaEncryptorController() {
    rsa = new RSA();
  }

  public RSA getRsa() {
    return rsa;
  }

  public void setEncryptPanel(EncryptPanel encryptPanel) {
    this.encryptPanel = encryptPanel;
  }

  public void setDecryptPanel(DecryptPanel decryptPanel) {
    this.decryptPanel = decryptPanel;
  }

  public void setKeyPanel(KeyPanel keyPanel) {
    this.keyPanel = keyPanel;
  }

  public long generateKeys() {
    long startTime = System.nanoTime();

    PrivateKey privateKey = new PrivateKey();
    PublicKey publicKey = privateKey.getPublicKey();
    rsa.setPrivateKey(privateKey);
    rsa.setPublicKey(publicKey);

    long endTime = System.nanoTime();
    return endTime - startTime;
  }

  public void setRsaPublicKey(String xml) throws SAXException, IOException {
    PublicKey key = PublicKey.fromXmlString(xml);
    rsa.setPublicKey(key);
  }

  public void setRsaPrivateKey(String xml) throws SAXException, IOException {
    PrivateKey key = PrivateKey.fromXmlString(xml);
    rsa.setPrivateKey(key);
  }

  public String[] encryptText(String plainText) throws NullPointerException {
    return rsa.encrypt(plainText);
  }

  public String decryptText(String manifest, String encryptedText)
      throws SAXException, IOException, NullPointerException {
    return rsa.decrypt(manifest, encryptedText);
  }
}
