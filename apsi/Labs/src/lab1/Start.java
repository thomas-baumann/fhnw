package lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Start {

	public static void main(String[] args) throws IOException {

		TextGenerator tg = new TextGenerator();
		tg.loadTextFile("rsc/Patterns.txt");

		Hash hashFunction = new Hash();

		String textOriginal = readFile("rsc/original.txt");
		long hashOriginal = hashFunction.getHash(textOriginal);

		long hash;
		String text;
		do {
			text = tg.getNextText();
			hash = hashFunction.getHash(text);
		} while (hashOriginal != hash);

		System.out.println(hashOriginal);
		System.out.println(textOriginal);
		System.out.println();
		System.out.println(hash);
		System.out.println(text);

	}

	private static String readFile(String fileName) throws IOException {
		StringBuilder text = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line = reader.readLine();
		while (line != null) {
			text.append(line);
			line = reader.readLine();
		}
		reader.close();
		return text.toString();
	}
}
