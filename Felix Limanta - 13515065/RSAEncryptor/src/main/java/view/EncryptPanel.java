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
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;
import org.felixlimanta.RSAEncryptor.controller.RsaEncryptorController;

/**
 * Created by ASUS on 13/06/17.
 */
public class EncryptPanel {

  private JPanel rootPanel;
  private JFileChooser fileChooser;
  private RsaEncryptorController controller;

  private JTextField loadFileTextField;
  private JButton loadFileBrowseButton;

  private JTextArea plainTextArea;
  private JTextArea encryptedTextArea;

  private JButton encryptButton;
  private JButton saveManifestButton;
  private JButton saveEncryptedTextButton;
  private JLabel loadFilePathLabel;

  private String manifest;

  public EncryptPanel() {
    setUpOpenButtonListener();
    setUpEncryptButtonListener();
    setUpSaveManifestButtonListener();
    setUpSaveTextButtonListener();
  }

  public JPanel getRootPanel() {
    return rootPanel;
  }

  public void setFileChooser(JFileChooser fileChooser) {
    this.fileChooser = fileChooser;
  }

  public void setController(RsaEncryptorController controller) {
    this.controller = controller;
  }

  private void setUpOpenButtonListener() {
    loadFileBrowseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fileChooser.setDialogTitle("Open file to be encrypted");
        int returnVal = fileChooser.showOpenDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String path = fileChooser.getSelectedFile().getPath();
          loadFilePathLabel.setText(path);
          plainTextArea.setText(loadFileToString(path));
        }
      }
    });
  }

  private void setUpEncryptButtonListener() {
    encryptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (plainTextArea.getText().equals("")) {
          JOptionPane.showMessageDialog(rootPanel,
              "Plain text area is empty. Load text or type it in the text area first.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        try {
          String[] encrypted = controller.encryptText(plainTextArea.getText());
          manifest = encrypted[0];
          encryptedTextArea.setText(encrypted[1]);
        } catch (NullPointerException npe) {
          JOptionPane.showMessageDialog(rootPanel,
              "Public key is not set. Generate or load public key first.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          npe.printStackTrace();
        }
      }
    });
  }

  private void setUpSaveManifestButtonListener() {
    saveManifestButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (manifest == null) {
          JOptionPane.showMessageDialog(rootPanel,
              "Manifest is not set. Encrypt text first.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        fileChooser.setDialogTitle("Save manifest file");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
            "XML files", "xml")
        );
        fileChooser.setAcceptAllFileFilterUsed(true);
        int returnVal = fileChooser.showSaveDialog(rootPanel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          if (!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("xml")) {
            file = new File(file.toString() + ".xml");
          }
          saveStringToFile(file.getPath(), manifest);
        }

        fileChooser.resetChoosableFileFilters();
      }
    });
  }

  private void setUpSaveTextButtonListener() {
    saveEncryptedTextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (plainTextArea.getText().equals("")) {
          JOptionPane.showMessageDialog(rootPanel,
              "Encrypted text area is empty. Encrypt text first.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        fileChooser.setDialogTitle("Save encrypted file");
        int returnVal = fileChooser.showSaveDialog(rootPanel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String path = fileChooser.getSelectedFile().getPath();
          saveStringToFile(path, encryptedTextArea.getText());
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
