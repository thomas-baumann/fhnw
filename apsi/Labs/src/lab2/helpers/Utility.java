package lab2.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Utility {
	
	public static String generateRandomString(int size) {
		StringBuilder build = new StringBuilder();
		for (int i = 0; i < size; i++) {
			double rand = Math.random();
			char next = 'a';
			if (rand < 0.3) {
				next = (char) ('A' + (int)(Math.random() * 26));
			} else if (rand > 0.6) {
				next = (char) ('a' + (int)(Math.random() * 26));
			} else {
				next = (char) ('0' + (int)(Math.random() * 9));
			}
			build.append(next);
		}
		return build.toString();
	}
	
	public static String hashString(String in) {
		try {
			return new String(MessageDigest.getInstance("SHA-256").digest(in.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public static String generateHashedString() {
		return hashString(generateRandomString(32));
	}
	
}
