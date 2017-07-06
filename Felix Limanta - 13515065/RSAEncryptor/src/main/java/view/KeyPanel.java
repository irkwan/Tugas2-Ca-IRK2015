package org.felixlimanta.RSAEncryptor.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;
import org.felixlimanta.RSAEncryptor.controller.RsaEncryptorController;
import org.felixlimanta.RSAEncryptor.model.XmlHelper;

/**
 * Created by ASUS on 13/06/17.
 */
public class KeyPanel {

  private RsaEncryptorController controller;
  private JPanel rootPanel;

  private JButton generateKeysButton;

  private JButton publicKeyBrowseButton;
  private JLabel publicKeyPathLabel;
  private JTextArea publicKeyTextArea;

  private JButton privateKeyBrowseButton;
  private JLabel privateKeyPathLabel;
  private JTextArea privateKeyTextArea;
  private JButton savePublicKeyButton;
  private JButton savePrivateKeyButton;

  private JFileChooser fileChooser;

  public KeyPanel() {
    setUpFileChooser();
    setUpKeyGenerationButton();
    setUpPublicKeySaveButton();
    setUpPrivateKeySaveButton();
    setUpPublicKeyOpenButton();
    setUpPrivateKeyOpenButton();
  }

  public void setController(RsaEncryptorController controller) {
    this.controller = controller;
  }

  public JPanel getRootPanel() {
    return rootPanel;
  }

  private void setUpFileChooser() {
    fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
        "XML files", "xml")
    );
    fileChooser.setAcceptAllFileFilterUsed(true);
  }

  private void setUpKeyGenerationButton() {
    generateKeysButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        long elapsedTime = controller.generateKeys();

        publicKeyTextArea.setText(XmlHelper.formatXml(
            controller.getRsa().getPublicKey().toXmlString(), 4
        ));
        privateKeyTextArea.setText(XmlHelper.formatXml(
            controller.getRsa().getPrivateKey().toXmlString(), 4
        ));
        savePublicKeyButton.doClick();
        savePrivateKeyButton.doClick();

        JOptionPane.showMessageDialog(rootPanel,
            "Keys generated in " + elapsedTime / 1000000 + " ms.");
      }
    });
  }

  private void setUpPublicKeySaveButton() {
    savePublicKeyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String xml;
        try {
          xml = XmlHelper.formatXml(
              controller.getRsa().getPublicKey().toXmlString(), 4
          );
        } catch (NullPointerException npe) {
          JOptionPane.showMessageDialog(rootPanel,
              "Public key is not set. Generate or load public key first.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          npe.printStackTrace();
          return;
        }

        fileChooser.setDialogTitle("Save public key");
        int returnVal = fileChooser.showSaveDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          if (!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("xml")) {
            file = new File(file.toString() + ".xml");
          }

          try (FileWriter fw = new FileWriter(file)) {
            fw.write(xml);
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPanel,
                "Error saving file to specified path.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
          }
        }
      }
    });
  }

  private void setUpPrivateKeySaveButton() {
    savePrivateKeyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String xml;
        try {
          xml = XmlHelper.formatXml(
              controller.getRsa().getPrivateKey().toXmlString(), 4
          );
        } catch (NullPointerException npe) {
          JOptionPane.showMessageDialog(rootPanel,
              "Private key is not set. Generate or load private key first.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          npe.printStackTrace();
          return;
        }

        fileChooser.setDialogTitle("Save private key");
        int returnVal = fileChooser.showSaveDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          if (!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("xml")) {
            file = new File(file.toString() + ".xml");
          }

          try (FileWriter fw = new FileWriter(file)) {
            fw.write(xml);
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPanel,
                "Error saving file to specified path.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
          }
        }
      }
    });
  }

  private void setUpPublicKeyOpenButton() {
    publicKeyBrowseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fileChooser.setDialogTitle("Open public key");
        int returnVal = fileChooser.showOpenDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String path = fileChooser.getSelectedFile().getPath();
          publicKeyPathLabel.setText(path);
          String xml = loadFileAsString(path);
          xml = XmlHelper.formatXml(xml, 4);

          try {
            controller.setRsaPublicKey(xml);
            publicKeyTextArea.setText(xml);
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPanel,
                "XML cannot be parsed.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
          }
        }
      }
    });
  }

  private void setUpPrivateKeyOpenButton() {
    privateKeyBrowseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fileChooser.setDialogTitle("Open private key");
        int returnVal = fileChooser.showOpenDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String path = fileChooser.getSelectedFile().getPath();
          privateKeyPathLabel.setText(path);
          String xml = loadFileAsString(path);
          xml = XmlHelper.formatXml(xml, 4);

          try {
            controller.setRsaPrivateKey(xml);
            privateKeyTextArea.setText(xml);
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPanel,
                "XML cannot be parsed.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
          }
        }
      }
    });
  }

  private String loadFileAsString(String path) {
    try {
      byte[] contents = Files.readAllBytes(Paths.get(path));
      return new String(contents, "UTF-8");
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(rootPanel ,
          "File does not exist or cannot be opened.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      return "";
    }
  }
}
