package org.felixlimanta.RSAEncryptor.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.felixlimanta.RSAEncryptor.controller.RsaEncryptorController;

/**
 * Created by ASUS on 13/06/17.
 */
public class DecryptPanel {

  private RsaEncryptorController controller;

  private JPanel rootPanel;
  private JButton encryptedTextBrowseButton;
  private JButton manifestBrowseButton;
  private JTextArea encryptedTextArea;
  private JTextArea plainTextArea;
  private JButton decryptButton;
  private JButton saveDecryptedButton;
  private JLabel encryptedTextPathLabel;
  private JLabel manifestPathLabel;

  private JFileChooser fileChooser;

  private String manifest;


  public DecryptPanel() {
    setUpOpenTextButtonListener();
    setUpOpenManifestButtonListener();
    setUpSaveButtonListener();
    setUpDecryptButtonListener();
  }

  public JPanel getRootPanel() {
    return rootPanel;
  }

  public void setController(RsaEncryptorController controller) {
    this.controller = controller;
  }

  public void setFileChooser(JFileChooser fileChooser) {
    this.fileChooser = fileChooser;
  }

  private void setUpOpenTextButtonListener() {
    encryptedTextBrowseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fileChooser.setDialogTitle("Open encrypted file");
        int returnVal = fileChooser.showOpenDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String path = fileChooser.getSelectedFile().getPath();
          encryptedTextPathLabel.setText(path);
          encryptedTextArea.setText(loadFileToString(path));
        }
      }
    });
  }

  private void setUpOpenManifestButtonListener() {
    manifestBrowseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fileChooser.setDialogTitle("Open manifest");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
            "XML files", "xml")
        );
        fileChooser.setAcceptAllFileFilterUsed(true);
        int returnVal = fileChooser.showOpenDialog(rootPanel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String path = fileChooser.getSelectedFile().getPath();
          manifestPathLabel.setText(path);
          manifest = loadFileToString(path);
        }
        fileChooser.resetChoosableFileFilters();
      }
    });
  }

  private void setUpDecryptButtonListener() {
    decryptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (encryptedTextArea.getText().equals("")) {
          JOptionPane.showMessageDialog(rootPanel,
              "Encrypted text area is empty. Load text or type it in the text area first.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (manifest == null) {
          JOptionPane.showMessageDialog(rootPanel,
              "Manifest not found. Open manifest file to decrypt text.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        try {
          String decrypted = controller.decryptText(manifest, encryptedTextArea.getText());
          plainTextArea.setText(decrypted);
        } catch (NullPointerException npe) {
          JOptionPane.showMessageDialog(rootPanel,
              "Public key is not set. Generate or load public key first.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          npe.printStackTrace();
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(rootPanel,
              "Manifest XML cannot be parsed.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }
      }
    });
  }

  private void setUpSaveButtonListener() {
    saveDecryptedButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (encryptedTextArea.getText().equals("")) {
          JOptionPane.showMessageDialog(rootPanel,
              "Plain area is empty. Decrypt text first.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        fileChooser.setDialogTitle("Save decrypted file");
        int returnVal = fileChooser.showSaveDialog(rootPanel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String path = fileChooser.getSelectedFile().getPath();
          saveStringToFile(path, plainTextArea.getText());
        }
      }
    });
  }

  private String loadFileToString(String path) {
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

  private void saveStringToFile(String path, String text) {
    try (FileWriter fw = new FileWriter(path)) {
      fw.write(text);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(rootPanel,
          "Error saving file to specified path.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
  }
}
