package rsa;

import rsa.RSAPublicKey;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

  public PrivateKeyContentsDialog(RSAPrivateKey privateKey) {
    setContentPane(contentPane);
    setModal(true);
    setTitle("Public key contents");
    getRootPane().setDefaultButton(buttonOK);
    modulusTextArea.setText(privateKey.getModulusText());
    publicExponentTextArea.setText(privateKey.getPublicExponentText());
    privateExponentTextArea.setText(privateKey.getPrivateExponentText());
    prime1TextArea.setText(privateKey.getPrime1Text());
    prime2TextArea.setText(privateKey.getPrime2Text());
    //exponent1TextArea.setText(privateKey.getExponent1Text());
    //exponent2TextArea.setText(privateKey.getExponent2Text());
    //coefficientTextArea.setText(privateKey.getCoefficientText());

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
