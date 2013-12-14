package lab2.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public final class Verifiers {

	public static boolean verifyPostcode(String postcode) {
		// check post.ch
		try {
			String postNotFoundMessage = "Keine PLZ gefunden";
			String urlPost = "http://www.post.ch/db/owa/pv_plz_pack/pr_check_data?p_language=de&p_nap=" + postcode
					+ "&p_localita=&p_cantone=&p_tipo=luogo";
			return verifyPostCode(urlPost, postNotFoundMessage);
		} catch (IOException e) {
			// do nothing, check on page postleitzahlen.ch
		}

		// check postleitzahlen.ch
		try {
			String postleitzahlenNotFoundMessage = "Leider keine Suchresultate erzielt";
			String urlPostleitzahlen = "http://www.postleitzahlen.ch/cgi-bin/plz.cgi?query=" + postcode
					+ "&land=Schweiz&submit.x=0&submit.y=0";
			return verifyPostCode(urlPostleitzahlen, postleitzahlenNotFoundMessage);
		} catch (IOException e) {
			// if both websites are not available assume postcode is correct
			return true;
		}
	}

	private static boolean verifyPostCode(String urlString, String notFoundMessage) throws IOException {
		URL url = new URL(urlString);
		BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
		String line = r.readLine();
		boolean notFound = true;
		while (line != null) {
			if (line.contains(notFoundMessage)) {
				// notFoundMessage found, return false
				notFound = false;
				break;
			}
			line = r.readLine();
		}
		r.close();
		// notFoundMessage not found, return true
		return notFound;
	}

}
