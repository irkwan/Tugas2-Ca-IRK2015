package org.felixlimanta.RSAEncryptor.view;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

/**
 * Created by ASUS on 13/06/17.
 */
public class BaseView extends JFrame {

  private EncryptPanel encryptPanelObj;
  private DecryptPanel decryptPanelObj;
  private KeyPanel keyPanelObj;

  private JTabbedPane tabbedPane;
  private JPanel rootPanel;
  private JPanel encryptPanel;
  private JPanel decryptPanel;
  private JPanel keyPanel;

  private JFileChooser fileChooser;

  public BaseView() {
    super("RSA Encryptor");
    setUpFileChooser();

    setContentPane(rootPanel);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    pack();
    setVisible(true);
  }

  private void createUIComponents() {
    encryptPanelObj = new EncryptPanel();
    decryptPanelObj = new DecryptPanel();
    keyPanelObj = new KeyPanel();

    encryptPanel = encryptPanelObj.getRootPanel();
    decryptPanel = decryptPanelObj.getRootPanel();
    keyPanel = keyPanelObj.getRootPanel();
  }

  private void setUpFileChooser() {
    fileChooser = new JFileChooser();
    encryptPanelObj.setFileChooser(fileChooser);
  }
}
