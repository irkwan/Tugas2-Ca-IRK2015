package rsa;

import rsa.RSAPublicKey;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;

public class PrivateKeyContentsDialog extends JDialog {
  private JPanel contentPane;
  private JButton buttonOK;
  private JTextArea modulusTextArea;
  private JTextArea publicExponentTextArea;
  private JTextArea privateExponentTextArea;
  private JTextArea prime1TextArea;
  private JTextArea prime2TextArea;
  private JTextArea exponent1TextArea;
  private JTextArea exponent2TextArea;
  private JTextArea coefficientTextArea;
  private JLabel keyLengthLabel;
  private JLabel messageLimitLabel;

  public PrivateKeyContentsDialog (RSAPrivateKey privateKey, String hashAlgorithm) throws NoSuchAlgorithmException {
    setContentPane(contentPane);
    setModal(true);
    setTitle("Private key contents");
    getRootPane().setDefaultButton(buttonOK);
    keyLengthLabel.setText(String.format("Key length: %d bits", privateKey.getModulusByteCount() * 8));
    messageLimitLabel.setText(String.format("Message limit: %d bytes", Message.getLengthLimit(hashAlgorithm, privateKey.getModulusByteCount())));
    modulusTextArea.setText(privateKey.getModulusText());
    publicExponentTextArea.setText(privateKey.getPublicExponentText());
    privateExponentTextArea.setText(privateKey.getPrivateExponentText());
    prime1TextArea.setText(privateKey.getPrime1Text());
    prime2TextArea.setText(privateKey.getPrime2Text());
    exponent1TextArea.setText(privateKey.getExponent1Text());
    exponent2TextArea.setText(privateKey.getExponent2Text());
    coefficientTextArea.setText(privateKey.getCoefficientText());

    buttonOK.addActionListener(e -> {
      setVisible(false);
      dispose();
    });
  }

  public void showDialog() {
    pack();
    setModal(true);
    setVisible(true);
  }
}
