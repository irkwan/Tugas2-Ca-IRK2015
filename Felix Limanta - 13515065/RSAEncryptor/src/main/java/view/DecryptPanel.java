package org.felixlimanta.RSAEncryptor.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Created by ASUS on 13/06/17.
 */
public class DecryptPanel {

  private JPanel rootPanel;
  private JTextField encryptedTextPathField;
  private JButton encryptedTextOpenButton;
  private JButton encryptedTextBrowseButton;
  private JTextField manifestPathField;
  private JButton manifestOpenButton;
  private JButton manifestBrowseButton;
  private JTextArea encryptedTextArea;
  private JTextArea plainTextArea;
  private JButton decryptButton;
  private JButton saveDecryptedButton;


  public JPanel getRootPanel() {
    return rootPanel;
  }
}
