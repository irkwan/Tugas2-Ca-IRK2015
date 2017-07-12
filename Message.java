package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.math.BigInteger;

public class Message {

	private String filePath;
	private String alphabetmessage;
	private BigInteger numericmessage;

	public Message(String filePath) {
		this.filePath = filePath;
		alphabetmessage = readMessage();
		numericmessage = transformToNumber();
	}

	private String readMessage() {
		String s = "";
		try {
			Scanner input = new Scanner(new File(filePath));
			while (input.hasNextLine()) {
				s += input.nextLine() + ((input.hasNextLine()) ? "\n" : "");
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return s;
	}

	private BigInteger transformToNumber() {
		return new BigInteger(alphabetmessage.getBytes());
	}

	public BigInteger getMessage() {
		return numericmessage;
	}
	
	public String decodeMessage(BigInteger message) {
		return new String(message.toByteArray());
	}

}