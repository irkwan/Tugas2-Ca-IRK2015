import java.awt.Font;

import javax.swing.*;

public class ReadOnlyTextField extends JTextArea {

	public ReadOnlyTextField() {
		setEditable(false);
		setLineWrap(true);
		setFont(new Font("Arial", Font.PLAIN, 15));
		setVisible(true);
	}

}