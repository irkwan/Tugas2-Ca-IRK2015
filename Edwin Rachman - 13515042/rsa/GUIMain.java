package rsa;

import com.sun.javaws.exceptions.InvalidArgumentException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

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
    keyPair = null;
    encodingComboBox.putClientProperty("revertChange", null);
    refreshControls();

    generateNewKeyPairButton.addActionListener(e -> {
      RSAKeyPair newKeyPair = new GenerateNewKeyPairDialog().showDialog();
      if (newKeyPair != null) {
        keyPair = newKeyPair;
      }
      refreshControls();
    });

    viewPublicKeyButton.addActionListener(e -> new PublicKeyContentsDialog(keyPair.getPublicKey()).showDialog());

    viewPrivateKeyButton.addActionListener(e -> new PrivateKeyContentsDialog(keyPair.getPrivateKey()).showDialog());


    loadMessageButton.addActionListener(e -> {
      try {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          message = Message.fromFile(fileChooser.getSelectedFile());
        }
        messageTextArea.setText(message.toString((String) encodingComboBox.getSelectedItem()));
      }
      catch (UnsupportedEncodingException ex) {
        JOptionPane.showMessageDialog(contentPane, "Encoding unsupported", "Load message from file", JOptionPane.ERROR_MESSAGE);
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File open error", "Load message from file", JOptionPane.ERROR_MESSAGE);
      }
    });


    saveMessageButton.addActionListener(e -> {
      try {
        message = Message.fromString(messageTextArea.getText(), (String) encodingComboBox.getSelectedItem());
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          message.toFile(fileChooser.getSelectedFile());
        }
      }
      catch (IllegalArgumentException | UnsupportedEncodingException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid characters", "Save message to file", JOptionPane.ERROR_MESSAGE);
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File save error", "Save message to file", JOptionPane.ERROR_MESSAGE);
      }
    });

    encodingComboBox.addItemListener(e -> {
      if (e.getStateChange() == ItemEvent.DESELECTED) {
        try {
          if (encodingComboBox.getClientProperty("revertChange") == null) {
            message = Message.fromString(messageTextArea.getText(), (String) e.getItem());
          }
        }
        catch (IllegalArgumentException | UnsupportedEncodingException ex) {
          JOptionPane.showMessageDialog(contentPane, "Invalid characters", "Encoding change", JOptionPane.ERROR_MESSAGE);
          encodingComboBox.putClientProperty("revertChange", e.getItem());
        }
      }
      else if (e.getStateChange() == ItemEvent.SELECTED) {
        try {
          if (encodingComboBox.getClientProperty("revertChange") == null) {
            messageTextArea.setText(message.toString((String) e.getItem()));
          }
          else {
            encodingComboBox.setSelectedItem(encodingComboBox.getClientProperty("revertChange"));
            encodingComboBox.putClientProperty("revertChange", null);
          }
        }
        catch (UnsupportedEncodingException ex) {
          JOptionPane.showMessageDialog(contentPane, "Encoding unsupported", "Encoding change", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    savePublicKeyButton.addActionListener(e -> {
      try {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          keyPair.savePublicKeyFile(fileChooser.getSelectedFile());
        }
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File save error", "Save public key to file", JOptionPane.ERROR_MESSAGE);
      }
    });

    savePrivateKeyButton.addActionListener(e -> {
      try {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          keyPair.savePrivateKeyFile(fileChooser.getSelectedFile());
        }
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File save error", "Save private key to file", JOptionPane.ERROR_MESSAGE);
      }
    });

    loadPublicKeyButton.addActionListener(e -> {
      try {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          keyPair = RSAKeyPair.fromPublicKeyFile(fileChooser.getSelectedFile());
        }
        refreshControls();
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File load error", "Load public key from file", JOptionPane.ERROR_MESSAGE);
      }
      catch (ClassNotFoundException ex) {
        JOptionPane.showMessageDialog(contentPane, "Class not found - fatal error", "Load public key from file", JOptionPane.ERROR_MESSAGE);
        System.exit(-1);
      }
    });

    loadPrivateKeyButton.addActionListener(e -> {
      try {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          keyPair = RSAKeyPair.fromPrivateKeyFile(fileChooser.getSelectedFile());
        }
        refreshControls();
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File load error", "Load private key from file", JOptionPane.ERROR_MESSAGE);
      }
      catch (ClassNotFoundException ex) {
        JOptionPane.showMessageDialog(contentPane, "Class not found - fatal error", "Load private key from file", JOptionPane.ERROR_MESSAGE);
        System.exit(-1);
      }
    });

    encryptMessageButton.addActionListener(e -> {
      try {
        encodingComboBox.setSelectedItem("Hex");

        long startTime = System.nanoTime();
        message = Message.fromString(messageTextArea.getText(), (String) encodingComboBox.getSelectedItem());
        message = keyPair.encrypt(message, "", "UTF-8", "SHA-1");
        messageTextArea.setText(message.toString((String) encodingComboBox.getSelectedItem()));
        long endTime = System.nanoTime();

        JOptionPane.showMessageDialog(contentPane, String.format("Message encryption finished in %.2f ms", (endTime - startTime) / 1000000.0), "Decrypt message", JOptionPane.INFORMATION_MESSAGE);
      }
      catch (RSAException ex) {
        JOptionPane.showMessageDialog(contentPane, ex.getMessage(), "Encrypt message", JOptionPane.ERROR_MESSAGE);
      }
      catch (IllegalArgumentException | UnsupportedEncodingException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid characters", "Encrypt message", JOptionPane.ERROR_MESSAGE);
      }
      catch (NoSuchAlgorithmException | IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "Fatal error - invalid algorithm", "Encrypt message", JOptionPane.ERROR_MESSAGE);
        System.exit(-1);
      }
    });

    decryptMessageButton.addActionListener(e -> {
      try {
        encodingComboBox.setSelectedItem("Hex");

        long startTime = System.nanoTime();
        message = Message.fromString(messageTextArea.getText(), (String) encodingComboBox.getSelectedItem());
        message = keyPair.decrypt(message, "", "UTF-8", "SHA-1");
        messageTextArea.setText(message.toString((String) encodingComboBox.getSelectedItem()));
        long endTime = System.nanoTime();

        JOptionPane.showMessageDialog(contentPane, String.format("Message decryption finished in %.2f ms", (endTime - startTime) / 1000000.0), "Decrypt message", JOptionPane.INFORMATION_MESSAGE);
      }
      catch (RSAException ex) {
        JOptionPane.showMessageDialog(contentPane, ex.getMessage(), "Decrypt message", JOptionPane.ERROR_MESSAGE);
      }
      catch (IllegalArgumentException | UnsupportedEncodingException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid characters", "Decrypt message", JOptionPane.ERROR_MESSAGE);
      }
      catch (NoSuchAlgorithmException | IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "Fatal error - invalid algorithm", "Decrypt message", JOptionPane.ERROR_MESSAGE);
        System.exit(-1);
      }
    });

  }

  public void refreshControls () {
    viewPublicKeyButton.setEnabled(keyPair != null);
    viewPrivateKeyButton.setEnabled(keyPair != null && keyPair.hasPrivateKey());
    savePublicKeyButton.setEnabled(keyPair != null);
    savePrivateKeyButton.setEnabled(keyPair != null && keyPair.hasPrivateKey());
    encryptMessageButton.setEnabled(keyPair != null);
    decryptMessageButton.setEnabled(keyPair != null && keyPair.hasPrivateKey());
  }

  public static void main(String[] args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    JFrame frame = new JFrame("RSA Encrypt/Decrypt");
    frame.setContentPane(new GUIMain().contentPane);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
