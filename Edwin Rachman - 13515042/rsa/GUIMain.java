package rsa;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

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
  private boolean editLock;

  public GUIMain () {
    keyPair = null;
    editLock = false;
    encodingComboBox.putClientProperty("prevItem", null);
    encodingComboBox.putClientProperty("blockChange", false);
    encodingComboBox.putClientProperty("beforeEncryptItem", null);
    refreshControls();

    generateNewKeyPairButton.addActionListener(e -> {
      RSAKeyPair newKeyPair = new GenerateNewKeyPairDialog().showDialog();
      if (newKeyPair != null) {
        keyPair = newKeyPair;
      }
      refreshControls();
    });

    viewPublicKeyButton.addActionListener(e -> {
      try {
        new PublicKeyContentsDialog(keyPair.getPublicKey(), "SHA-1").showDialog();
      }
      catch (NoSuchAlgorithmException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid algorithm", "View public key contents", JOptionPane.ERROR_MESSAGE);
      }
    });

    viewPrivateKeyButton.addActionListener(e -> {
      try {
        new PrivateKeyContentsDialog(keyPair.getPrivateKey(), "SHA-1").showDialog();
      }
        catch (NoSuchAlgorithmException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid algorithm", "View private key contents", JOptionPane.ERROR_MESSAGE);
      }
    });

    loadMessageButton.addActionListener(e -> {
      try {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load message from file");
        if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          message = Message.fromFile(fileChooser.getSelectedFile());
          try {
            messageTextArea.setText(message.toString((String) encodingComboBox.getSelectedItem()));
            JOptionPane.showMessageDialog(contentPane, "Message successfully loaded", "Load message from file", JOptionPane.INFORMATION_MESSAGE);
          }
          catch (CharacterCodingException ex) {
            encodingComboBox.setSelectedItem("Hex");
            message = Message.fromFile(fileChooser.getSelectedFile());
            messageTextArea.setText(message.toString((String) encodingComboBox.getSelectedItem()));
          }
        }
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File load error", "Load message from file", JOptionPane.ERROR_MESSAGE);
      }
    });

    saveMessageButton.addActionListener(e -> {
      try {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save message to file");
        if (fileChooser.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          message = Message.fromString(messageTextArea.getText(), (String) encodingComboBox.getSelectedItem());
          message.toFile(fileChooser.getSelectedFile());
          JOptionPane.showMessageDialog(contentPane, "Message successfully saved", "Save message from file", JOptionPane.INFORMATION_MESSAGE);
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
        encodingComboBox.putClientProperty("previousItem", e.getItem());
      }
      else if (e.getStateChange() == ItemEvent.SELECTED) {
        Object prevEncoding = encodingComboBox.getClientProperty("previousItem");
        Object curEncoding = e.getItem();
        encodingComboBox.putClientProperty("previousItem", null);

        if (!prevEncoding.equals(curEncoding) && !(boolean) encodingComboBox.getClientProperty("blockChange")) {
          try {
            message = Message.fromString(messageTextArea.getText(), (String) prevEncoding);
            messageTextArea.setText(message.toString((String) curEncoding));
          }
          catch (IllegalArgumentException | UnsupportedEncodingException | CharacterCodingException ex) {
            JOptionPane.showMessageDialog(contentPane, "Invalid characters", "Encoding change", JOptionPane.ERROR_MESSAGE);
            encodingComboBox.putClientProperty("blockChange", true);
            encodingComboBox.setSelectedItem(prevEncoding);
          }
        }
        else {
          encodingComboBox.putClientProperty("blockChange", false);
        }
      }
    });

    savePublicKeyButton.addActionListener(e -> {
      try {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save private key to file");
        if (fileChooser.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          keyPair.savePublicKeyFile(fileChooser.getSelectedFile());
          JOptionPane.showMessageDialog(contentPane, "Public key successfully saved", "Save public key to file", JOptionPane.INFORMATION_MESSAGE);
        }
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File save error", "Save public key to file", JOptionPane.ERROR_MESSAGE);
      }
    });

    savePrivateKeyButton.addActionListener(e -> {
      try {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save private key to file");
        if (fileChooser.showSaveDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          keyPair.savePrivateKeyFile(fileChooser.getSelectedFile());
          JOptionPane.showMessageDialog(contentPane, "Private key successfully saved", "Save private key to file", JOptionPane.INFORMATION_MESSAGE);
        }
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File save error", "Save private key to file", JOptionPane.ERROR_MESSAGE);
      }
    });

    loadPublicKeyButton.addActionListener(e -> {
      try {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load public key from file");
        if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          keyPair = RSAKeyPair.fromPublicKeyFile(fileChooser.getSelectedFile());
          JOptionPane.showMessageDialog(contentPane, "Public key successfully loaded", "Load public key from file", JOptionPane.INFORMATION_MESSAGE);
          refreshControls();
        }
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File load error", "Load public key from file", JOptionPane.ERROR_MESSAGE);
      }
      catch (ClassCastException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid public key file", "Load public key from file", JOptionPane.ERROR_MESSAGE);
      }
      catch (ClassNotFoundException ex) {
        JOptionPane.showMessageDialog(contentPane, "Class not found", "Load public key from file", JOptionPane.ERROR_MESSAGE);
      }
    });

    loadPrivateKeyButton.addActionListener(e -> {
      try {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load private key from file");
        if (fileChooser.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
          keyPair = RSAKeyPair.fromPrivateKeyFile(fileChooser.getSelectedFile());
          JOptionPane.showMessageDialog(contentPane, "Private key successfully loaded", "Load private key from file", JOptionPane.INFORMATION_MESSAGE);
          refreshControls();
        }
      }
      catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File load error", "Load private key from file", JOptionPane.ERROR_MESSAGE);
      }
      catch (ClassCastException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid private key file", "Load private key from file", JOptionPane.ERROR_MESSAGE);
      }
      catch (ClassNotFoundException ex) {
        JOptionPane.showMessageDialog(contentPane, "Class not found", "Load private key from file", JOptionPane.ERROR_MESSAGE);
      }
    });

    encryptMessageButton.addActionListener(e -> {
      encodingComboBox.putClientProperty("beforeEncryptItem", encodingComboBox.getSelectedItem());
      encodingComboBox.setSelectedItem("Hex");
      editLock = true;
      encryptMessageButton.setText("Encrypting...");
      refreshControls();

      new SwingWorker<Void, Void>() {
        private long startTime, endTime;

        @Override
        protected Void doInBackground() throws Exception {
          startTime = System.nanoTime();
          message = Message.fromString(messageTextArea.getText(), (String) encodingComboBox.getSelectedItem());
          message = keyPair.encrypt(message, "", "UTF-8", "SHA-1");
          messageTextArea.setText(message.toString((String) encodingComboBox.getSelectedItem()));
          return null;
        }

        @Override
        protected void done() {
          try {
            endTime = System.nanoTime();
            editLock = false;
            encryptMessageButton.setText("Encrypt message");
            refreshControls();
            get();
            JOptionPane.showMessageDialog(contentPane, String.format("Message encryption finished in %.2f ms", (endTime - startTime) / 1000000.0), "Decrypt message", JOptionPane.INFORMATION_MESSAGE);
          }
          catch (InterruptedException | ExecutionException ex) {
            Throwable ee = ex.getCause();

            if (ee instanceof RSAException) {
              encodingComboBox.setSelectedItem(encodingComboBox.getClientProperty("beforeEncryptItem"));
              encodingComboBox.putClientProperty("beforeEncryptItem", null);
              JOptionPane.showMessageDialog(contentPane, ee.getMessage(), "Encrypt message", JOptionPane.ERROR_MESSAGE);
            }
            else if (ee instanceof IllegalArgumentException || ee instanceof UnsupportedEncodingException) {
              JOptionPane.showMessageDialog(contentPane, "Invalid characters", "Encrypt message", JOptionPane.ERROR_MESSAGE);
            }
            else if (ee instanceof NoSuchAlgorithmException || ee instanceof IOException) {
              JOptionPane.showMessageDialog(contentPane, "Invalid algorithm", "Encrypt message", JOptionPane.ERROR_MESSAGE);
            }
          }
        }
      }.execute();
    });

    decryptMessageButton.addActionListener(e -> {
      editLock = true;
      decryptMessageButton.setText("Decrypting...");
      refreshControls();

      new SwingWorker<Void, Void>() {
        private long startTime, endTime;

        @Override
        protected Void doInBackground() throws Exception {
          startTime = System.nanoTime();
          message = Message.fromString(messageTextArea.getText(), (String) encodingComboBox.getSelectedItem());
          message = keyPair.decrypt(message, "", "UTF-8", "SHA-1");
          messageTextArea.setText(message.toString((String) encodingComboBox.getSelectedItem()));
          return null;
        }

        @Override
        protected void done() {
          try {
            endTime = System.nanoTime();
            editLock = false;
            decryptMessageButton.setText("Decrypt message");
            refreshControls();
            get();
            encodingComboBox.setSelectedItem(encodingComboBox.getClientProperty("beforeEncryptItem"));
            encodingComboBox.putClientProperty("beforeEncryptItem", null);
            JOptionPane.showMessageDialog(contentPane, String.format("Message decryption finished in %.2f ms", (endTime - startTime) / 1000000.0), "Decrypt message", JOptionPane.INFORMATION_MESSAGE);
          }
          catch (InterruptedException | ExecutionException ex) {
            Throwable ee = ex.getCause();

            if (ee instanceof RSAException) {
              JOptionPane.showMessageDialog(contentPane, ee.getMessage(), "Decrypt message", JOptionPane.ERROR_MESSAGE);
            }
            else if (ee instanceof IllegalArgumentException || ee instanceof UnsupportedEncodingException) {
              JOptionPane.showMessageDialog(contentPane, "Invalid characters", "Decrypt message", JOptionPane.ERROR_MESSAGE);
            }
            else if (ee instanceof NoSuchAlgorithmException || ee instanceof IOException) {
              JOptionPane.showMessageDialog(contentPane, "Invalid algorithm", "Decrypt message", JOptionPane.ERROR_MESSAGE);
            }
          }
        }
      }.execute();
    });

  }

  public void refreshControls () {
    generateNewKeyPairButton.setEnabled(!editLock);
    loadPublicKeyButton.setEnabled(!editLock);
    loadPrivateKeyButton.setEnabled(!editLock);
    viewPublicKeyButton.setEnabled(keyPair != null);
    viewPrivateKeyButton.setEnabled(keyPair != null && keyPair.hasPrivateKey());
    savePublicKeyButton.setEnabled(keyPair != null);
    savePrivateKeyButton.setEnabled(keyPair != null && keyPair.hasPrivateKey());
    loadMessageButton.setEnabled(!editLock);
    encryptMessageButton.setEnabled(keyPair != null && !editLock);
    decryptMessageButton.setEnabled(keyPair != null && keyPair.hasPrivateKey() && !editLock);
    encodingComboBox.setEnabled(!editLock);
    messageTextArea.setEnabled(!editLock);
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
