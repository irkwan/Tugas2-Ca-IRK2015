package rsa;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Edwin on 7/9/2017.
 */
public class GUIMain {
  private JButton generateNewKeyPairButton;
  private JButton loadPublicKeyButton;
  private JButton loadPrivateKeyButton;
  private JTextArea messageTextArea;
  private JButton savePublicKeyButton;
  private JButton savePrivateKeyButton;
  private JButton viewPublicKeyButton;
  private JButton viewPrivateKeyButton;
  private JComboBox encodingComboBox;
  private JButton loadMessageButton;
  private JButton saveMessageButton;
  private JButton encryptMessageButton;
  private JButton decryptMessageButton;
  private JPanel contentPane;

  private RSAKeyPair keyPair;
  private Message message;

  public GUIMain () {
    generateNewKeyPairButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        RSAKeyPair newKeyPair = new GenerateNewKeyPairDialog().showDialog();
        if (newKeyPair != null) {
          keyPair = newKeyPair;
        }
        refreshControls();
      }
    });

    viewPublicKeyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new PublicKeyContentsDialog(keyPair.getPublicKey()).showDialog();
      }
    });


    loadMessageButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          try {
            message = Message.fromFile(fileChooser.getSelectedFile());
          }
          catch (IOException ex) {
            JOptionPane.showMessageDialog(contentPane, "File open error", "Load message from file", JOptionPane.ERROR_MESSAGE);
          }
        }
        messageTextArea.setText(message.toHexString());
      }
    });


    saveMessageButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          try {
            message.toFile(fileChooser.getSelectedFile());
          }
          catch (IOException ex) {
            JOptionPane.showMessageDialog(contentPane, "File save error", "Save message to file", JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });

    keyPair = null;
    refreshControls();
  }

  public void refreshControls () {
    viewPublicKeyButton.setEnabled(keyPair != null);
    viewPrivateKeyButton.setEnabled(keyPair != null);
    savePublicKeyButton.setEnabled(keyPair != null);
    savePrivateKeyButton.setEnabled(keyPair != null);
    encryptMessageButton.setEnabled(keyPair != null);
    decryptMessageButton.setEnabled(keyPair != null);
  }

  public static void main(String[] args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    JFrame frame = new JFrame("RSA Encrypt/Decrypt");
    frame.setContentPane(new GUIMain().contentPane);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
