import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Message {

	private String filePath;
	private String alphabetmessage;
	private BigNum numericmessage;

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
	
	private BigNum transformToNumber() {
		BigNum result = new BigNum(), temp;
		for (int i = alphabetmessage.length() - 1; i >= 0; i--) {
			temp = new BigNum(new int[]{alphabetmessage.charAt(i)});
			result = result.shiftLeft(8).or(temp);
		}
		return result;
	}
	
	public BigNum getMessage() {
		return numericmessage;
	}
	
	public int getMessageLength() {
		return alphabetmessage.length();
	}
	
	public String decodeMessage(BigNum message) {
		String s = "";
		BigNum mask = new BigNum(new int[]{0xFF}), temp = message;
		while (!temp.equals(BigNum.ZERO)) {
			s += (char) temp.and(mask).toIntArray()[0];
			temp = temp.shiftRight(8);
		}
		return s;
	}

}
