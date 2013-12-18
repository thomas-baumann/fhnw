package lab2.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Utility {

	public static String generateRandomString(int size) {
		StringBuilder build = new StringBuilder();
		for (int i = 0; i < size; i++) {
			double rand = Math.random();
			char next;
			if (rand < 0.3) {
				next = (char) ('A' + (int) (Math.random() * 26));
			} else if (rand > 0.6) {
				next = (char) ('a' + (int) (Math.random() * 26));
			} else {
				next = (char) ('0' + (int) (Math.random() * 10));
			}
			build.append(next);
		}
		return build.toString();
	}

	/**
	 * Returns a string with a length of 64 chars
	 * 
	 * @param in
	 * @return
	 */
	public static String hashString(String in) {
		// http://www.java-forum.org/spezialthemen/16968-hashfunktionen-java-messagedigest.html
		try {
			byte[] hash = MessageDigest.getInstance("SHA-256").digest(in.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < hash.length; ++i) {
				sb.append(Integer.toHexString((hash[i] & 0xFF) | 0x100).toLowerCase().substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}
