package lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TextGenerator {

	private ArrayList<Integer> iterated = new ArrayList<Integer>();
	private String[][] text;
	private int nr = -1;
	Random rand = new Random();
	private boolean randomize;
	
	public TextGenerator(String fileName, boolean randomize) {
		try {
			this.loadTextFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.randomize = randomize;
	}

	private void loadTextFile(String fileName) throws IOException {
		// get the number of lines
		BufferedReader lineReader = new BufferedReader(new FileReader(fileName));
		int lines = 0;
		while (lineReader.readLine() != null) {
			lines++;
		}
		lineReader.close();

		this.text = new String[lines][2];

		// read all lines and fill them into the text array
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line;
		int lineNr = 0;
		line = reader.readLine();
		while (line != null) {
			int pos = line.indexOf("|");
			if (pos >= 0) {
				// seperator found
				// add first part on position 0
				this.text[lineNr][0] = line.substring(0, pos);
				// add second part on positon 1
				this.text[lineNr][1] = line.substring(pos + 1, line.length());
			} else {
				// no seperator found, add the same text on both sides 
				this.text[lineNr][0] = line;
				this.text[lineNr][1] = line;
			}
			line = reader.readLine();
			lineNr++;
		}
		reader.close();
	}

	public String getNextText() {
		if (randomize) {
			// search random number which wasn't used
			int localNr;
			do {
				localNr = rand.nextInt();
			} while(iterated.contains(localNr));
			iterated.add(localNr);
			// get the text with this binary represenation
			return this.getText(localNr);
		} else {
			// get the text with the binary representation of the current number
			return this.getText(this.nr--);
		}
		
	}

	public String getText(int nr) {
		// get binary representation of the integer
		String binary = Integer.toBinaryString(nr);

		StringBuilder st = new StringBuilder();

		int lengthBinary = binary.length();
		int lengthZeros = 32 - lengthBinary;

		// add the text for all leading 0, which are not visible in the binary string
		for (int i = 0; i < lengthZeros; i++) {
			st.append(this.text[i][0]);
		}
		// add the text for all bits of the binary string
		for (int i = 0; i < lengthBinary; i++) {
			st.append(this.text[i + lengthZeros][(binary.charAt(i)) - 48]);
		}
		// return the build string
		return st.toString();
	}
}
