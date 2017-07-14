package rsa;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileOutputStream;
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
  private BigNumber publicExponent;
  private String hashAlgorithm;
  private String label;
  private String labelCharset;

  public GUIMain() {
    File configFile = new File("config.xml");
    if (!configFile.exists() || configFile.isDirectory()) {
      makeDefaultConfigFile(configFile);
    }
    loadConfigFile(configFile);

    keyPair = null;
    editLock = false;
    encodingComboBox.putClientProperty("prevItem", null);
    encodingComboBox.putClientProperty("blockChange", false);
    encodingComboBox.putClientProperty("beforeEncryptItem", null);
    refreshControls();

    generateNewKeyPairButton.addActionListener(e -> {
      RSAKeyPair newKeyPair = new GenerateNewKeyPairDialog(publicExponent, hashAlgorithm).showDialog();
      if (newKeyPair != null) {
        keyPair = newKeyPair;
      }
      refreshControls();
    });

    viewPublicKeyButton.addActionListener(e -> {
      try {
        new PublicKeyContentsDialog(keyPair.getPublicKey(), hashAlgorithm).showDialog();
      } catch (NoSuchAlgorithmException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid algorithm", "View public key contents", JOptionPane.ERROR_MESSAGE);
      }
    });

    viewPrivateKeyButton.addActionListener(e -> {
      try {
        new PrivateKeyContentsDialog(keyPair.getPrivateKey(), hashAlgorithm).showDialog();
      } catch (NoSuchAlgorithmException ex) {
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
          } catch (CharacterCodingException ex) {
            encodingComboBox.setSelectedItem("Hex");
            message = Message.fromFile(fileChooser.getSelectedFile());
            messageTextArea.setText(message.toString((String) encodingComboBox.getSelectedItem()));
          }
        }
      } catch (IOException ex) {
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
      } catch (IllegalArgumentException | UnsupportedEncodingException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid characters", "Save message to file", JOptionPane.ERROR_MESSAGE);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File save error", "Save message to file", JOptionPane.ERROR_MESSAGE);
      }
    });

    encodingComboBox.addItemListener(e -> {
      if (e.getStateChange() == ItemEvent.DESELECTED) {
        encodingComboBox.putClientProperty("previousItem", e.getItem());
      } else if (e.getStateChange() == ItemEvent.SELECTED) {
        Object prevEncoding = encodingComboBox.getClientProperty("previousItem");
        Object curEncoding = e.getItem();
        encodingComboBox.putClientProperty("previousItem", null);

        if (!prevEncoding.equals(curEncoding) && !(boolean) encodingComboBox.getClientProperty("blockChange")) {
          try {
            message = Message.fromString(messageTextArea.getText(), (String) prevEncoding);
            messageTextArea.setText(message.toString((String) curEncoding));
          } catch (IllegalArgumentException | UnsupportedEncodingException | CharacterCodingException ex) {
            JOptionPane.showMessageDialog(contentPane, "Invalid characters", "Encoding change", JOptionPane.ERROR_MESSAGE);
            encodingComboBox.putClientProperty("blockChange", true);
            encodingComboBox.setSelectedItem(prevEncoding);
          }
        } else {
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
      } catch (IOException ex) {
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
      } catch (IOException ex) {
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
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File load error", "Load public key from file", JOptionPane.ERROR_MESSAGE);
      } catch (ClassCastException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid public key file", "Load public key from file", JOptionPane.ERROR_MESSAGE);
      } catch (ClassNotFoundException ex) {
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
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(contentPane, "File load error", "Load private key from file", JOptionPane.ERROR_MESSAGE);
      } catch (ClassCastException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid private key file", "Load private key from file", JOptionPane.ERROR_MESSAGE);
      } catch (ClassNotFoundException ex) {
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
          message = keyPair.encrypt(message, label, labelCharset, hashAlgorithm);
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
          } catch (InterruptedException | ExecutionException ex) {
            encodingComboBox.setSelectedItem(encodingComboBox.getClientProperty("beforeEncryptItem"));
            encodingComboBox.putClientProperty("beforeEncryptItem", null);
            Throwable ee = ex.getCause();

            if (ee instanceof RSAException) {
              JOptionPane.showMessageDialog(contentPane, ee.getMessage(), "Encrypt message", JOptionPane.ERROR_MESSAGE);
            } else if (ee instanceof IllegalArgumentException || ee instanceof UnsupportedEncodingException) {
              JOptionPane.showMessageDialog(contentPane, "Invalid characters", "Encrypt message", JOptionPane.ERROR_MESSAGE);
            } else if (ee instanceof NoSuchAlgorithmException || ee instanceof IOException) {
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
          message = keyPair.decrypt(message, label, labelCharset, hashAlgorithm);
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
          } catch (InterruptedException | ExecutionException ex) {
            Throwable ee = ex.getCause();

            if (ee instanceof RSAException) {
              JOptionPane.showMessageDialog(contentPane, ee.getMessage(), "Decrypt message", JOptionPane.ERROR_MESSAGE);
            } else if (ee instanceof IllegalArgumentException || ee instanceof UnsupportedEncodingException) {
              JOptionPane.showMessageDialog(contentPane, "Invalid characters", "Decrypt message", JOptionPane.ERROR_MESSAGE);
            } else if (ee instanceof NoSuchAlgorithmException || ee instanceof IOException) {
              JOptionPane.showMessageDialog(contentPane, "Invalid algorithm", "Decrypt message", JOptionPane.ERROR_MESSAGE);
            }
          }
        }
      }.execute();
    });

  }

  public void refreshControls() {
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

  public void makeDefaultConfigFile(File file) {
    try {
      Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      Element root = dom.createElement("config");
      Element e;
      e = dom.createElement("public-exponent");
      e.appendChild(dom.createTextNode("65537"));
      root.appendChild(e);
      e = dom.createElement("hash-algorithm");
      e.appendChild(dom.createTextNode("SHA-1"));
      root.appendChild(e);
      e = dom.createElement("label");
      e.appendChild(dom.createTextNode(""));
      root.appendChild(e);
      e = dom.createElement("label-charset");
      e.appendChild(dom.createTextNode("UTF-8"));
      root.appendChild(e);
      dom.appendChild(root);

      TransformerFactory.newInstance().newTransformer().transform(new DOMSource(dom), new StreamResult(new FileOutputStream(file)));
    } catch (ParserConfigurationException | TransformerException ex) {
      JOptionPane.showMessageDialog(contentPane, "XML process error", "Make default config file", JOptionPane.ERROR_MESSAGE);
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(contentPane, "File make error", "Make default config file", JOptionPane.ERROR_MESSAGE);
    }
  }

  private String getXMLElement(Element root, String tagName) {
    try {
      return root.getElementsByTagName(tagName).item(0).getFirstChild().getNodeValue();
    } catch (NullPointerException ex) {
      return "";
    }
  }

  public void loadConfigFile(File file) {
    try {
      Element root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).getDocumentElement();
      publicExponent = BigNumber.fromInt(Integer.parseInt(getXMLElement(root, "public-exponent")));
      hashAlgorithm = getXMLElement(root, "hash-algorithm");
      label = getXMLElement(root, "label");
      labelCharset = getXMLElement(root, "label-charset");

      if (!hashAlgorithm.equals("SHA-1") && !hashAlgorithm.equals("SHA-256")) {
        JOptionPane.showMessageDialog(contentPane, "Invalid algorithm", "Load config file", JOptionPane.ERROR_MESSAGE);
        System.exit(-1);
      }
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(contentPane, "Invalid public exponent", "Load config file", JOptionPane.ERROR_MESSAGE);
      System.exit(-1);
    } catch (ParserConfigurationException | SAXException ex) {
      JOptionPane.showMessageDialog(contentPane, "XML process error", "Load config file", JOptionPane.ERROR_MESSAGE);
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(contentPane, "File load error", "Load config file", JOptionPane.ERROR_MESSAGE);
    }
  }

  public static void main(String[] args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    JFrame frame = new JFrame("RSA Encrypt/Decrypt");
    frame.setContentPane(new GUIMain().contentPane);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    contentPane = new JPanel();
    contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 2, new Insets(10, 10, 10, 10), -1, -1));
    generateNewKeyPairButton = new JButton();
    generateNewKeyPairButton.setText("Generate new key pair");
    contentPane.add(generateNewKeyPairButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    loadPublicKeyButton = new JButton();
    loadPublicKeyButton.setText("Load public key from file");
    contentPane.add(loadPublicKeyButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    loadPrivateKeyButton = new JButton();
    loadPrivateKeyButton.setText("Load private key from file");
    contentPane.add(loadPrivateKeyButton, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    savePublicKeyButton = new JButton();
    savePublicKeyButton.setEnabled(false);
    savePublicKeyButton.setText("Save public key to file");
    contentPane.add(savePublicKeyButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    savePrivateKeyButton = new JButton();
    savePrivateKeyButton.setEnabled(false);
    savePrivateKeyButton.setText("Save private key to file");
    contentPane.add(savePrivateKeyButton, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    viewPublicKeyButton = new JButton();
    viewPublicKeyButton.setEnabled(false);
    viewPublicKeyButton.setText("View public key contents");
    contentPane.add(viewPublicKeyButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label1 = new JLabel();
    label1.setText("Message:");
    contentPane.add(label1, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    viewPrivateKeyButton = new JButton();
    viewPrivateKeyButton.setEnabled(false);
    viewPrivateKeyButton.setText("View private key contents");
    contentPane.add(viewPrivateKeyButton, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    encryptMessageButton = new JButton();
    encryptMessageButton.setEnabled(false);
    encryptMessageButton.setText("Encrypt message");
    contentPane.add(encryptMessageButton, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    decryptMessageButton = new JButton();
    decryptMessageButton.setEnabled(false);
    decryptMessageButton.setText("Decrypt message");
    contentPane.add(decryptMessageButton, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    loadMessageButton = new JButton();
    loadMessageButton.setText("Load message from file");
    contentPane.add(loadMessageButton, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    saveMessageButton = new JButton();
    saveMessageButton.setText("Save message to file");
    contentPane.add(saveMessageButton, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JScrollPane scrollPane1 = new JScrollPane();
    contentPane.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(200, 100), null, 0, false));
    messageTextArea = new JTextArea();
    messageTextArea.setEnabled(true);
    messageTextArea.setLineWrap(true);
    messageTextArea.setText("");
    scrollPane1.setViewportView(messageTextArea);
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JLabel label2 = new JLabel();
    label2.setText("Encoding:");
    panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    encodingComboBox = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
    defaultComboBoxModel1.addElement("UTF-8");
    defaultComboBoxModel1.addElement("Hex");
    defaultComboBoxModel1.addElement("ASCII");
    defaultComboBoxModel1.addElement("UTF-16");
    defaultComboBoxModel1.addElement("UTF-32");
    encodingComboBox.setModel(defaultComboBoxModel1);
    panel1.add(encodingComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPane;
  }
}
