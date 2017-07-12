package rsa;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PublicKeyContentsDialog extends JDialog {
  private JPanel contentPane;
  private JButton buttonOK;
  private JTextArea modulusTextArea;
  private JTextArea publicExponentTextArea;

  public PublicKeyContentsDialog(RSAPublicKey publicKey) {
    setContentPane(contentPane);
    setModal(true);
    setTitle("Public key contents");
    getRootPane().setDefaultButton(buttonOK);
    modulusTextArea.setText(publicKey.getModulusText());
    publicExponentTextArea.setText(publicKey.getPublicExponentText());

    buttonOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
      }
    });
  }

  public void showDialog() {
    pack();
    setModal(true);
    setVisible(true);
  }
}
