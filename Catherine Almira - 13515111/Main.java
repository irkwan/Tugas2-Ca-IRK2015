import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
/**
 *
 * @author Catherine Almira / 13515111
 */
public class Main extends javax.swing.JFrame {

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
    }

    @SuppressWarnings("unchecked")                     
    private void initComponents() {

        rsaKeysPanel = new javax.swing.JPanel();
        generateKey = new javax.swing.JButton();
        primeNumber1TextField = new javax.swing.JTextField();
        primeNumber2TextField = new javax.swing.JTextField();
        primeNumber1Label = new javax.swing.JLabel();
        primeNumber2Label = new javax.swing.JLabel();
        encryptionKeyTextField = new javax.swing.JTextField();
        decryptionKeyTextField = new javax.swing.JTextField();
        encryptionKeyLabel = new javax.swing.JLabel();
        decryptionKeyLabel = new javax.swing.JLabel();
        plainTextPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        plainTextArea = new javax.swing.JTextArea();
        browseButton = new javax.swing.JButton();
        encryptedTextPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        encryptedTextArea = new javax.swing.JTextArea();
        encryptButton = new javax.swing.JButton();
        decryptedTextPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        decryptedTextArea = new javax.swing.JTextArea();
        decryptButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        
        this.setTitle("RSA Application by Catherine Almira - 13515111");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        rsaKeysPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("RSA Keys"));

        generateKey.setText("Generate Keys");
        generateKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateKeyActionPerformed(evt);
            }
        });

        primeNumber1TextField.setEditable(false);

        primeNumber2TextField.setEditable(false);

        primeNumber1Label.setText("Prime Number 1:");

        primeNumber2Label.setText("Prime Number 2:");

        encryptionKeyTextField.setEditable(false);

        decryptionKeyTextField.setEditable(false);
        
        encryptionKeyLabel.setText("Encryption Key:");

        decryptionKeyLabel.setText("Decryption Key:");

        javax.swing.GroupLayout rsaKeysPanelLayout = new javax.swing.GroupLayout(rsaKeysPanel);
        rsaKeysPanel.setLayout(rsaKeysPanelLayout);
        rsaKeysPanelLayout.setHorizontalGroup(
            rsaKeysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rsaKeysPanelLayout.createSequentialGroup()
                .addGroup(rsaKeysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rsaKeysPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(rsaKeysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(primeNumber1Label)
                            .addComponent(primeNumber2Label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(rsaKeysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(primeNumber1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(primeNumber2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                        .addGroup(rsaKeysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(decryptionKeyLabel)
                            .addComponent(encryptionKeyLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(rsaKeysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(encryptionKeyTextField)
                            .addComponent(decryptionKeyTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)))
                    .addComponent(generateKey, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        rsaKeysPanelLayout.setVerticalGroup(
            rsaKeysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rsaKeysPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(rsaKeysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(primeNumber1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(primeNumber1Label)
                    .addComponent(encryptionKeyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(encryptionKeyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rsaKeysPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(primeNumber2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(primeNumber2Label)
                    .addComponent(decryptionKeyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(decryptionKeyLabel))
                .addGap(9, 9, 9)
                .addComponent(generateKey)
                .addGap(345, 345, 345))
        );

        plainTextPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Plain Text"));
        plainTextPanel.setPreferredSize(new java.awt.Dimension(210, 345));

        jScrollPane1.setAutoscrolls(true);

        plainTextArea.setEditable(false);
        plainTextArea.setColumns(20);
        plainTextArea.setRows(5);
        jScrollPane1.setViewportView(plainTextArea);

        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout plainTextPanelLayout = new javax.swing.GroupLayout(plainTextPanel);
        plainTextPanel.setLayout(plainTextPanelLayout);
        plainTextPanelLayout.setHorizontalGroup(
            plainTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(browseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        plainTextPanelLayout.setVerticalGroup(
            plainTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plainTextPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(browseButton)
                .addContainerGap())
        );

        encryptedTextPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Encrypted Text"));
        encryptedTextPanel.setPreferredSize(new java.awt.Dimension(210, 345));

        jScrollPane3.setAutoscrolls(true);

        encryptedTextArea.setEditable(false);
        encryptedTextArea.setColumns(20);
        encryptedTextArea.setRows(5);
        jScrollPane3.setViewportView(encryptedTextArea);

        encryptButton.setText("Encrypt");
        encryptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encryptButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout encryptedTextPanelLayout = new javax.swing.GroupLayout(encryptedTextPanel);
        encryptedTextPanel.setLayout(encryptedTextPanelLayout);
        encryptedTextPanelLayout.setHorizontalGroup(
            encryptedTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
            .addComponent(encryptButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        encryptedTextPanelLayout.setVerticalGroup(
            encryptedTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(encryptedTextPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(encryptButton)
                .addContainerGap())
        );

        decryptedTextPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Decrypted Text"));
        decryptedTextPanel.setPreferredSize(new java.awt.Dimension(210, 345));

        jScrollPane2.setAutoscrolls(true);

        decryptedTextArea.setEditable(false);
        decryptedTextArea.setColumns(20);
        decryptedTextArea.setRows(5);
        jScrollPane2.setViewportView(decryptedTextArea);

        decryptButton.setText("Decrypt");
        decryptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decryptButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout decryptedTextPanelLayout = new javax.swing.GroupLayout(decryptedTextPanel);
        decryptedTextPanel.setLayout(decryptedTextPanelLayout);
        decryptedTextPanelLayout.setHorizontalGroup(
            decryptedTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
            .addComponent(decryptButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        decryptedTextPanelLayout.setVerticalGroup(
            decryptedTextPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(decryptedTextPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decryptButton)
                .addContainerGap())
        );

        jMenu1.setText("File");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText("New");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(newMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rsaKeysPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(plainTextPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(encryptedTextPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(decryptedTextPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(rsaKeysPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(plainTextPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .addComponent(encryptedTextPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .addComponent(decryptedTextPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void generateKeyActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if (!isAlreadyGenerated) {
            long startGenerationKeyTime = System.currentTimeMillis();
            rsa = new RSAGenerator();
            this.primeNumber1TextField.setText(rsa.getPrimeP());
            this.primeNumber2TextField.setText(rsa.getPrimeQ());
            this.decryptionKeyTextField.setText(rsa.getDeKey());
            this.encryptionKeyTextField.setText(rsa.getEnKey());
            long endGenerationKeyTime = System.currentTimeMillis();
            generationKeyTime = endGenerationKeyTime - startGenerationKeyTime;
            isAlreadyGenerated = true;
            JOptionPane.showMessageDialog(this, "Generation keys time: " + String.valueOf(generationKeyTime) + " ms", "Execution time", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "The keys have been generated." + "\n" +
                "Generation keys time: " + String.valueOf(generationKeyTime) + " ms", "Message" , JOptionPane.INFORMATION_MESSAGE);
        }
        
    }                                                     

    private void decryptButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        if (isAlreadyEncrypted && !isAlreadyDecrypted) {
            //dekripsi
            long startDecryptionTime = System.currentTimeMillis();
            decrypted = new BigInteger[encrypted.length];
            for (int i = 0; i < encrypted.length; i++) {
                decrypted[i] = rsa.decrypt(encrypted[i]);
            }
            long endDecryptionTime = System.currentTimeMillis();
            byte[] temp = RSAGenerator.convertBigIntToASCII(decrypted);
            decryptedTextArea.setText(RSAGenerator.convertASCIIToText(temp));
            decryptionTime = endDecryptionTime - startDecryptionTime;
            isAlreadyDecrypted = true;
            JOptionPane.showMessageDialog(this, "Decryption time: " + String.valueOf(decryptionTime) + " ms" +
                "\n" + "Encryption time: " + String.valueOf(encryptionTime) + "ms" +
                "\n" + "Generation keys time: " + String.valueOf(generationKeyTime) + " ms" + "\n" +
                "Total: " + String.valueOf(encryptionTime + generationKeyTime + decryptionTime) + " ms", "Execution time", JOptionPane.PLAIN_MESSAGE);
        } else if (isAlreadyDecrypted) {
            JOptionPane.showMessageDialog(this, "The text have been decrypted." + "\n" +
                "Decryption time: " + String.valueOf(decryptionTime) + " ms", "Message", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Encrypt before decrypt.", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
            
    }                                             

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if (!isAlreadyBrowse) {
            fc = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileFilter(filter);

            int returnVal = fc.showOpenDialog(Main.this);
            File file;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    file = fc.getSelectedFile();
                    BufferedReader in = new BufferedReader(new FileReader(file));
                    String line = in.readLine();
                    while(line != null) {
                        plainTextArea.append(line + "\n");
                        line = in.readLine();
                    }
                    fileContent = RSAGenerator.readFile(file);
                    content2 = RSAGenerator.convertASCIIToBigInt2(fileContent); //dalam bentuk array BigInteger
                    isAlreadyBrowse = true;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "File not found!", "Error", JOptionPane.INFORMATION_MESSAGE);
                }   
            }
        } else {
            JOptionPane.showMessageDialog(this, "The text have been chosen.", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }                                            

    private void encryptButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        if (isAlreadyBrowse && isAlreadyGenerated && !isAlreadyEncrypted) {
            //start timer
            long startEncryptionTime = System.currentTimeMillis();
            //enkripsi
            encrypted = new BigInteger[content2.length];
            for (int i = 0; i < content2.length; i++) {
                encrypted[i] = rsa.encrypt(content2[i]);
                //System.out.println(encrypted[i]);
            }
            //stop timer (enkripsi selesai)
            long endEncryptionTime = System.currentTimeMillis();
            for (int i = 0; i < encrypted.length; i++) {
                encryptedTextArea.append(encrypted[i].toString() + "\n");
            }
            encryptionTime = endEncryptionTime - startEncryptionTime;
            JOptionPane.showMessageDialog(this, "Encryption time: " + String.valueOf(encryptionTime) + "ms" +
                "\n" + "Generation keys time: " + String.valueOf(generationKeyTime) + " ms" + "\n" +
                "Total: " + String.valueOf(encryptionTime + generationKeyTime) + " ms", "Execution time", JOptionPane.PLAIN_MESSAGE);
            isAlreadyEncrypted = true;
        } else if (isAlreadyEncrypted) {
            JOptionPane.showMessageDialog(this, "The text have been encrypted." + "\n" + 
                "Encryption time: " + String.valueOf(encryptionTime) + " ms" + "\n", "Message", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Generate keys and browse the text before encrypt.", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {                                            
        Main reset = new Main();
        primeNumber1TextField.setText("");
        primeNumber2TextField.setText("");
        encryptionKeyTextField.setText("");
        decryptionKeyTextField.setText("");
        plainTextArea.setText("");
        decryptedTextArea.setText("");
        encryptedTextArea.setText("");
        isAlreadyEncrypted = false;
        isAlreadyDecrypted = false;
        isAlreadyBrowse = false;
        isAlreadyGenerated = false;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
    private RSAGenerator rsa;
    private JFileChooser fc;
    private byte[] fileContent;
    private BigInteger[] content2;
    private BigInteger[] encrypted;
    private BigInteger[] decrypted;
    private boolean isAlreadyGenerated = false;
    private boolean isAlreadyEncrypted = false;
    private boolean isAlreadyDecrypted = false;
    private boolean isAlreadyBrowse = false;
    private long encryptionTime;
    private long decryptionTime;
    private long generationKeyTime;

    private javax.swing.JButton browseButton;
    private javax.swing.JButton decryptButton;
    private javax.swing.JTextArea decryptedTextArea;
    private javax.swing.JPanel decryptedTextPanel;
    private javax.swing.JLabel decryptionKeyLabel;
    private javax.swing.JTextField decryptionKeyTextField;
    private javax.swing.JButton encryptButton;
    private javax.swing.JTextArea encryptedTextArea;
    private javax.swing.JPanel encryptedTextPanel;
    private javax.swing.JLabel encryptionKeyLabel;
    private javax.swing.JTextField encryptionKeyTextField;
    private javax.swing.JButton generateKey;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JTextArea plainTextArea;
    private javax.swing.JPanel plainTextPanel;
    private javax.swing.JLabel primeNumber1Label;
    private javax.swing.JTextField primeNumber1TextField;
    private javax.swing.JLabel primeNumber2Label;
    private javax.swing.JTextField primeNumber2TextField;
    private javax.swing.JPanel rsaKeysPanel;            
}
