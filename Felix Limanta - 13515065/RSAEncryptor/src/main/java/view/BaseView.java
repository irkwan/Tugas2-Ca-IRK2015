package org.felixlimanta.RSAEncryptor.view;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import org.felixlimanta.RSAEncryptor.controller.RsaEncryptorController;

/**
 * Top-level container for all View classes
 *
 * @author Felix Limanta
 * @version 1.0
 * @since 2017-06-13
 */
public class BaseView extends JFrame {

  private RsaEncryptorController controller;
  private EncryptPanel encryptPanelObj;
  private DecryptPanel decryptPanelObj;
  private KeyPanel keyPanelObj;

  private JPanel rootPanel;
  private JTabbedPane tabbedPane;
  private JPanel encryptPanel;
  private JPanel decryptPanel;
  private JPanel keyPanel;

  private JFileChooser fileChooser;

  /**
   * Initializes components, sets up file chooser and controller, and make panel visible
   */
  public BaseView() {
    super("RSA Encryptor");
    setUpFileChooser();
    setUpController();

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
    decryptPanelObj.setFileChooser(fileChooser);
  }

  private void setUpController() {
    controller = new RsaEncryptorController();
    encryptPanelObj.setController(controller);
    decryptPanelObj.setController(controller);
    keyPanelObj.setController(controller);
  }
}
