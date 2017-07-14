import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class RSAGUI extends JFrame {
	
	BigNum m;
	BigNum encryption;
	RSA rsa;
	boolean fileChosen = false;
	boolean RSACreated = false;

	public RSAGUI() {
		super("RSA");
		setSize(780, 550);
		getContentPane().setBackground(Color.CYAN);
		setLayout(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JFileChooser browse = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
		browse.setFileFilter(filter);
		JLabel time = new JLabel("Execution Time :                    ms");
		time.setFont(new Font("Arial", Font.PLAIN, 20));
		time.setBounds(145, 70, 300, 20);
		add(time);
		JLabel maxchar = new JLabel("Text Max : 40 Characters");
		maxchar.setFont(new Font("Arial", Font.PLAIN, 10));
		maxchar.setBounds(25, 5, 200, 10);
		add(maxchar);
		JLabel modulus = new JLabel("Modulus");
		modulus.setFont(new Font("Arial", Font.PLAIN, 20));
		modulus.setBounds(25, 110, 150, 20);
		add(modulus);
		JLabel privkey = new JLabel("Private Key");
		privkey.setFont(new Font("Arial", Font.PLAIN, 20));
		privkey.setBounds(25, 190, 150, 20);
		add(privkey);
		JLabel pubkey = new JLabel("Public Key");
		pubkey.setFont(new Font("Arial", Font.PLAIN, 20));
		pubkey.setBounds(25, 270, 150, 20);
		add(pubkey);
		JLabel encrypted = new JLabel("Encrypted");
		encrypted.setFont(new Font("Arial", Font.PLAIN, 20));
		encrypted.setBounds(25, 350, 150, 20);
		add(encrypted);
		JLabel decrypted = new JLabel("Decrypted");
		decrypted.setFont(new Font("Arial", Font.PLAIN, 20));
		decrypted.setBounds(25, 430, 150, 20);
		add(decrypted);
		ReadOnlyTextField dir = new ReadOnlyTextField();
		dir.setFont(new Font("Arial", Font.PLAIN, 20));
		dir.setBounds(25, 20, 600, 30);
		add(dir);
		ReadOnlyTextField ext = new ReadOnlyTextField();
		ext.setFont(new Font("Arial", Font.PLAIN, 20));
		ext.setBounds(300, 65, 100, 30);
		add(ext);
		ReadOnlyTextField mod = new ReadOnlyTextField();
		mod.setBounds(25, 135, 710, 35);
		add(mod);
		ReadOnlyTextField prk = new ReadOnlyTextField();
		prk.setBounds(25, 215, 710, 35);
		add(prk);
		ReadOnlyTextField pbk = new ReadOnlyTextField();
		pbk.setBounds(25, 295, 710, 35);
		add(pbk);
		ReadOnlyTextField enc = new ReadOnlyTextField();
		enc.setBounds(25, 375, 710, 35);
		add(enc);
		ReadOnlyTextField dec = new ReadOnlyTextField();
		dec.setBounds(25, 455, 710, 35);
		add(dec);
		JButton browsebutton = new JButton("Browse");
		browsebutton.setFont(new Font("Arial", Font.PLAIN, 16));
		browsebutton.setBounds(635, 20, 100, 30);
		browsebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browse.showOpenDialog(new JFrame());
				String path = browse.getSelectedFile().getAbsolutePath();
				dir.setText(path);
				m = new Message(path).getMessage();
				fileChosen = true;
			}
		});
		add(browsebutton);
		JButton clearbutton = new JButton("Clear");
		clearbutton.setFont(new Font("Arial", Font.PLAIN, 16));
		clearbutton.setBounds(635, 65, 100, 30);
		clearbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChosen = false;
				RSACreated = false;
				dir.setText(null);
				ext.setText(null);
				mod.setText(null);
				prk.setText(null);
				pbk.setText(null);
				enc.setText(null);
				dec.setText(null);
			}
		});
		add(clearbutton);
		JButton encryptbutton = new JButton("Encrypt");
		encryptbutton.setFont(new Font("Arial", Font.PLAIN, 16));
		encryptbutton.setBounds(25, 65, 100, 30);
		encryptbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileChosen) {
					double startTime, stopTime, executionTime;
					startTime = System.currentTimeMillis();
					rsa = new RSA();
					encryption = rsa.encrypt(m);
					stopTime = System.currentTimeMillis();
					executionTime = (stopTime - startTime);
					RSACreated = true;
					mod.setText(rsa.modulusToString());
					prk.setText(rsa.privKeyToString());
					pbk.setText(rsa.pubKeyToString());
					enc.setText(encryption.toString());
					ext.setText(String.valueOf(executionTime));
					revalidate();
					repaint();
				}
			}
		});
		add(encryptbutton);
		JButton decryptbutton = new JButton("Decrypt");
		decryptbutton.setFont(new Font("Arial", Font.PLAIN, 16));
		decryptbutton.setBounds(635, 420, 100, 30);
		decryptbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!RSACreated) {
					dec.setText("Text not yet encrypted!");
				}
				else {
					dec.setText(Message.decodeMessage(rsa.decrypt(encryption)));
				}
				revalidate();
				repaint();
			}
		});
		add(decryptbutton);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		RSAGUI rsagui = new RSAGUI();
	}

}