package rsa;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;

public class PublicKeyContentsDialog extends JDialog {
  private JPanel contentPane;
  private JButton buttonOK;
  private JTextArea modulusTextArea;
  private JTextArea publicExponentTextArea;
  private JLabel messageLimitLabel;
  private JLabel keyLengthLabel;

  public PublicKeyContentsDialog (RSAPublicKey publicKey, String hashAlgorithm) throws NoSuchAlgorithmException {
    setContentPane(contentPane);
    setModal(true);
    setTitle("Public key contents");
    getRootPane().setDefaultButton(buttonOK);
    keyLengthLabel.setText(String.format("Key length: %d bits", publicKey.getModulusByteCount() * 8));
    messageLimitLabel.setText(String.format("Message limit: %d bytes", Message.getLengthLimit(hashAlgorithm, publicKey.getModulusByteCount())));
    modulusTextArea.setText(publicKey.getModulusText());
    publicExponentTextArea.setText(publicKey.getPublicExponentText());

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
