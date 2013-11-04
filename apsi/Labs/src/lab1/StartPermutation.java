package lab1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class StartPermutation {

	public static void main(String[] args) throws IOException {
		int count = 5;
		int counter = 0;
		int block = 2048;
		int countFound = 0;

		// create the text generators for original and fake message
		TextGenerator tg = new TextGenerator("rsc/Patterns.txt", true);
		TextGenerator tgOrg = new TextGenerator("rsc/Patterns_Org.txt", true);

		// hashmap for hash and text of the fake message
		HashMap<Integer, String> hashesFake = new HashMap<Integer, String>();

		// hashmap for hash and text of the original message
		HashMap<Integer, String> hashesOrg = new HashMap<Integer, String>();

		// hashmap for all found collisions with the hash and list of all texts with this hash
		HashMap<Integer, ArrayList<String>> hashesFound = new HashMap<Integer, ArrayList<String>>();
		
		DESHash hashFunction = new DESHash();
		do {
			// Fill up new hashes
			for (int i = 0; i < block; i++) {
				String text = tg.getNextText();
				int hash = hashFunction.hash(text);
				hashesFake.put(hash, text);
				
				String text2 = tgOrg.getNextText();
				int hash2 = hashFunction.hash(text2);
				hashesOrg.put(hash2, text2);
			}

			// Collision detection
			Iterator<Integer> it = hashesFake.keySet().iterator();
			while(it.hasNext()) {
				int h = it.next();
				if (hashesOrg.containsKey(h)) { // a collision has been found
					if (hashesFound.containsKey(h)) { // this hash value was already found
						// add this message to an existing hash
						ArrayList<String> al = hashesFound.get(h);
						String t = hashesFake.get(h);
						String t2 = hashesOrg.get(h);
						if (!al.contains(t)) {
							countFound++;
							al.add(t);
							printResult(h, hashesOrg.get(h), hashesFake.get(h));
						}
						if (!al.contains(t2)) {
							al.add(t2);
						}
						hashesFound.put(h, al);
					} else { // this hash wasn't found before, add new entry
						countFound++;
						ArrayList<String> al = new ArrayList<String>();
						al.add(hashesOrg.get(h));
						al.add(hashesFake.get(h));
						hashesFound.put(h, al);
						printResult(h, hashesOrg.get(h), hashesFake.get(h));
					}
				}
			}
			counter += block;
			System.out.println(counter);
		} while (count != countFound);
		
		// Show us the hashes
		Iterator<Integer> it = hashesFound.keySet().iterator();
		while (it.hasNext()) {
			int h = it.next();
			ArrayList<String> al = hashesFound.get(h);
			System.out.println("Found Hash: " + h);
			System.out.println("Strings mapping to that Hash: ");
			for (String string : al) {
				System.out.println(string);
			}
			System.out.println("--------------------------------------------");
		}
	}
	
	private static void printResult(int h, String org, String fake) {
		DESHash hashFunc = new DESHash();
		System.out.println("Collision found!");
		System.out.println("Hash: " + h);
		System.out.println("Original: " + org);
		System.out.println("Fake: " + fake);
		int checkOrg = hashFunc.hash(org);
		int checkFake = hashFunc.hash(fake);
		System.out.println("Original Hash: " + checkOrg);
		System.out.println("Fake Hash: " + checkFake);
		System.out.println();
	}
}
