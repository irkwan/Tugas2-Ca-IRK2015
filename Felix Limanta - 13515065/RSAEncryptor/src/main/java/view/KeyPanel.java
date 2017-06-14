package org.felixlimanta.RSAEncryptor.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Created by ASUS on 13/06/17.
 */
public class KeyPanel {

  private JPanel rootPanel;
  private JTextField publicKeyPathField;
  private JButton publicKeyBrowseButton;
  private JButton publicKeyOpenButton;
  private JTextArea publicKeyTextArea;
  private JButton generateKeysButton;
  private JTextField privateKeyPathField;
  private JButton privateKeyOpenButton;
  private JButton privateKeyBrowseButton;
  private JTextArea privateKeyTextArea;

  public JPanel getRootPanel() {
    return rootPanel;
  }
}
