package org.felixlimanta.RSAEncryptor.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.felixlimanta.RSAEncryptor.controller.RsaEncryptorController;

/**
 * Created by ASUS on 13/06/17.
 */
public class EncryptPanel {

  private JPanel rootPanel;
  private JFileChooser fileChooser;
  private RsaEncryptorController control;
  
  private JLabel loadFileLabel;
  private JTextField loadFileTextField;
  private JButton loadFileOpenButton;
  private JButton loadFileBrowseButton;

  private JTextArea plainTextArea;
  private JTextArea encryptedTextArea;

  private JButton encryptButton;
  private JButton saveManifestButton;
  private JButton saveEncryptedTextButton;

  public EncryptPanel() {
    setUpLoadButtonsListener();
    setUpSaveButtonListener();
  }

  public JPanel getRootPanel() {
    return rootPanel;
  }

  public void setFileChooser(JFileChooser fileChooser) {
    this.fileChooser = fileChooser;
  }

  public void setControl(RsaEncryptorController control) {
    this.control = control;
  }

  private void setUpLoadButtonsListener() {
    loadFileOpenButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String path = loadFileTextField.getText();
        loadFileToTextArea(path);
      }
    });

    loadFileBrowseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int returnVal = fileChooser.showOpenDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String path = fileChooser.getSelectedFile().getPath();
          loadFileTextField.setText(path);
          loadFileToTextArea(path);
        }
      }
    });
  }

  private void setUpSaveButtonListener() {
    saveEncryptedTextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int returnVal = fileChooser.showSaveDialog(rootPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          String path = fileChooser.getSelectedFile().getPath();
          saveTextAreaToFile(path);
        }
      }
    });
  }

  private void loadFileToTextArea(String path) {
    try {
      byte[] contents = Files.readAllBytes(Paths.get(path));
      String text = new String(contents, "UTF-8");
      plainTextArea.setText(text);

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(rootPanel ,
          "File does not exist or cannot be opened.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void saveTextAreaToFile(String path) {
    try (PrintWriter out = new PrintWriter(path)  ){
      out.println(encryptedTextArea.getText());
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(rootPanel ,
          "File cannot be saved on selected directory.",
          "Error",
          JOptionPane.PLAIN_MESSAGE);
    }
  }
}
