package rsa;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.text.DefaultFormatter;
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
  private JComboBox publicExponentComboBox;

  private SwingWorker worker;
  private RSAKeyPair keyPair;

  public GenerateNewKeyPairDialog() {
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(loadButton);
    setTitle("Generate new key pair");
    ((JTextField) publicExponentComboBox.getEditor().getEditorComponent()).setHorizontalAlignment(JTextField.RIGHT);
    keyLengthSpinner.setModel(new SpinnerNumberModel(512, 384, null, 64));
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
        protected Void doInBackground () throws Exception {
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
            }
            else if (state == 1) {
              prime2 = BigNumber.generateRandom(BigNumber.getMaxValue((int) keyLengthSpinner.getValue() / 2));
              prime2.setBit(0);
              while (!isCancelled() && (!prime2.isPrimeSmallPrimesTest(128) || !prime2.isPrimeMillerRabinTest(5))) {
                prime2 = prime2.add(BigNumber.TWO);
                ++count;
                publish();
              }
            }
            else {
              try {
                keyPair = RSAKeyPair.generateKeyPair(prime1, prime2,
                    BigNumber.fromInt(Integer.parseInt((String) publicExponentComboBox.getSelectedItem())));
              }
              catch (RSAException ex) {
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
          }
          else if (state == 1) {
            firstPrimeStatusLabel.setText("First prime found");
            secondPrimeStatusLabel.setText(String.format("Finding second prime... %d", count));
            keyPairStatusLabel.setText("Key pair not yet generated");
          }
          else {
            firstPrimeStatusLabel.setText("First prime found");
            secondPrimeStatusLabel.setText("Second prime found");
            keyPairStatusLabel.setText("Key pair generated");
          }
        }

        @Override
        protected void done() {
          if (state == 3) {
            JOptionPane.showMessageDialog(contentPane, String.format("Key pair generation finished in %.2f ms", (endTime - startTime) / 1000000.0), "Generate new key pair", JOptionPane.INFORMATION_MESSAGE);
          }
          else {
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
        messageLimitLabel.setText(String.format("%d", Message.getLengthLimit("SHA-1", (int) keyLengthSpinner.getValue() / 8)));
      }
      catch (NoSuchAlgorithmException ex) {
        JOptionPane.showMessageDialog(contentPane, "Algorithm not found", "Generate new key pair", JOptionPane.ERROR_MESSAGE);
      }
    });
    keyLengthSpinner.getChangeListeners()[0].stateChanged(new ChangeEvent(keyLengthSpinner));
  }

  public void onCancel () {
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
}