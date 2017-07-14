package rsa;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class GenerateNewKeyPairDialog extends JDialog {
  private JPanel contentPane;
  private JButton loadButton;
  private JButton cancelButton;
  private JSpinner keyLengthSpinner;
  private JButton generateButton;
  private JLabel messageLimitLabel;
  private JButton abortButton;
  private JLabel firstPrimeStatusLabel;
  private JLabel secondPrimeStatusLabel;
  private JLabel keyPairStatusLabel;

  private SwingWorker worker;
  private RSAKeyPair keyPair;

  public GenerateNewKeyPairDialog(BigNumber publicExponent, String hashAlgorithm) {
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(loadButton);
    setTitle("Generate new key pair");
    int min = 64;
    try {
      while (Message.getLengthLimit(hashAlgorithm, min / 8) <= 0) {
        min += 64;
      }
    } catch (NoSuchAlgorithmException ex) {
      JOptionPane.showMessageDialog(contentPane, "Invalid algorithm", "Generate new key pair", JOptionPane.ERROR_MESSAGE);
    }

    keyLengthSpinner.setModel(new SpinnerNumberModel(min, min, null, 64));
    keyLengthSpinner.setEditor(new JSpinner.NumberEditor(keyLengthSpinner, "#"));
    ((DefaultFormatter) ((JFormattedTextField) keyLengthSpinner.getEditor().getComponent(0)).getFormatter()).setCommitsOnValidEdit(true);

    loadButton.addActionListener(e -> {
      setVisible(false);
      dispose();
    });

    cancelButton.addActionListener(e -> onCancel());

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    generateButton.addActionListener(e -> {
      keyLengthSpinner.setValue((int) keyLengthSpinner.getValue() - (int) keyLengthSpinner.getValue() % 64);

      worker = new SwingWorker<Void, Void>() {
        private int count;
        private int state;
        private long startTime;
        private long endTime;

        @Override
        protected Void doInBackground() throws Exception {
          count = 0;
          state = 0;
          BigNumber prime1 = null, prime2 = null;
          startTime = System.nanoTime();

          while (!isCancelled() && state < 3) {
            count = 0;
            publish();
            if (state == 0) {
              prime1 = BigNumber.generateRandom(BigNumber.getMaxValue((int) keyLengthSpinner.getValue() / 2));
              prime1.setBit(0);
              while (!isCancelled() && (!prime1.isPrimeSmallPrimesTest(128) || !prime1.isPrimeMillerRabinTest(5))) {
                prime1 = prime1.add(BigNumber.TWO);
                ++count;
                publish();
              }
            } else if (state == 1) {
              prime2 = BigNumber.generateRandom(BigNumber.getMaxValue((int) keyLengthSpinner.getValue() / 2));
              prime2.setBit(0);
              while (!isCancelled() && (!prime2.isPrimeSmallPrimesTest(128) || !prime2.isPrimeMillerRabinTest(5))) {
                prime2 = prime2.add(BigNumber.TWO);
                ++count;
                publish();
              }
            } else {
              try {
                keyPair = RSAKeyPair.generateKeyPair(prime1, prime2, publicExponent);
              } catch (RSAException ex) {
                state = -1;
              }
              endTime = System.nanoTime();
              publish();
            }
            ++state;
          }

          return null;
        }

        @Override
        protected void process(List<Void> chunks) {
          if (state == 0) {
            firstPrimeStatusLabel.setText(String.format("Finding first prime... %d", count));
            secondPrimeStatusLabel.setText("Second prime not yet found");
            keyPairStatusLabel.setText("Key pair not yet generated");
          } else if (state == 1) {
            firstPrimeStatusLabel.setText("First prime found");
            secondPrimeStatusLabel.setText(String.format("Finding second prime... %d", count));
            keyPairStatusLabel.setText("Key pair not yet generated");
          } else {
            firstPrimeStatusLabel.setText("First prime found");
            secondPrimeStatusLabel.setText("Second prime found");
            keyPairStatusLabel.setText("Key pair generated");
          }
        }

        @Override
        protected void done() {
          if (state == 3) {
            JOptionPane.showMessageDialog(contentPane, String.format("Key pair generation finished in %.2f ms", (endTime - startTime) / 1000000.0), "Generate new key pair", JOptionPane.INFORMATION_MESSAGE);
          } else {
            JOptionPane.showMessageDialog(contentPane, "Key pair generation aborted", "Generate new key pair", JOptionPane.WARNING_MESSAGE);
            firstPrimeStatusLabel.setText("First prime not yet found");
            secondPrimeStatusLabel.setText("Second prime not yet found");
            keyPairStatusLabel.setText("Key pair not yet generated");
          }
          generateButton.setText("Generate");
          generateButton.setEnabled(true);
          abortButton.setEnabled(false);
          loadButton.setEnabled(state == 3);
        }
      };

      generateButton.setText("Generating ...");
      generateButton.setEnabled(false);
      abortButton.setEnabled(true);
      loadButton.setEnabled(false);
      firstPrimeStatusLabel.setText("First prime not yet found");
      secondPrimeStatusLabel.setText("Second prime not yet found");
      keyPairStatusLabel.setText("Key pair not yet generated");
      worker.execute();
    });

    abortButton.addActionListener(e -> worker.cancel(true));

    keyLengthSpinner.addChangeListener(e -> {
      try {
        messageLimitLabel.setText(String.format("%d", Message.getLengthLimit(hashAlgorithm, (int) keyLengthSpinner.getValue() / 8)));
      } catch (NoSuchAlgorithmException ex) {
        JOptionPane.showMessageDialog(contentPane, "Invalid algorithm", "Generate new key pair", JOptionPane.ERROR_MESSAGE);
      }
    });
    keyLengthSpinner.getChangeListeners()[0].stateChanged(new ChangeEvent(keyLengthSpinner));
  }

  public void onCancel() {
    if (!generateButton.isEnabled()) {
      worker.cancel(true);
    }
    keyPair = null;
    setVisible(false);
    dispose();
  }

  public RSAKeyPair showDialog() {
    pack();
    setVisible(true);
    return keyPair;
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
    contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
    contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    loadButton = new JButton();
    loadButton.setEnabled(false);
    loadButton.setText("Load");
    panel2.add(loadButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    cancelButton = new JButton();
    cancelButton.setText("Cancel");
    panel2.add(cancelButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
    panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(100, -1), null, 0, false));
    firstPrimeStatusLabel = new JLabel();
    firstPrimeStatusLabel.setText("First prime not yet found");
    panel1.add(firstPrimeStatusLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    secondPrimeStatusLabel = new JLabel();
    secondPrimeStatusLabel.setText("Second prime not yet found");
    panel1.add(secondPrimeStatusLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    keyPairStatusLabel = new JLabel();
    keyPairStatusLabel.setText("Key pair not yet generated");
    panel1.add(keyPairStatusLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
    contentPane.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JLabel label1 = new JLabel();
    label1.setText("Key length: ");
    panel3.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label2 = new JLabel();
    label2.setText("bits");
    panel3.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label3 = new JLabel();
    label3.setText("Message limit:");
    panel3.add(label3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label4 = new JLabel();
    label4.setText("bytes");
    panel3.add(label4, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    messageLimitLabel = new JLabel();
    messageLimitLabel.setText("0");
    panel3.add(messageLimitLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    keyLengthSpinner = new JSpinner();
    panel3.add(keyLengthSpinner, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    contentPane.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    generateButton = new JButton();
    generateButton.setText("Generate");
    panel4.add(generateButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
    panel4.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    abortButton = new JButton();
    abortButton.setEnabled(false);
    abortButton.setText("Abort");
    panel4.add(abortButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPane;
  }
}