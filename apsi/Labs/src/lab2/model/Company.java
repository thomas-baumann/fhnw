package lab2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lab2.helpers.Utility;
import lab2.helpers.Verifiers;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.routines.EmailValidator;

/*
 * TODO: http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
 * http://www.unicode.org/reports/tr18/
 */
public class Company {

	private final List<String> errors = new ArrayList<>();

	private int id;
	private String username;
	private String password;
	private String passwordNotHashed;
	private final String email;
	private final String name;
	private final String address;
	private final String postcode;
	private final String town;
	private String hashCode;

	public Company(String username, String password) {
		this(0, username, null, null, null, null, null, null, null);
		this.setPassword(password);
	}

	public Company(String email, String name, String address, String zipcode, String town) {
		this(0, null, null, StringEscapeUtils.escapeHtml4(email), name, address, zipcode, town, null);
	}

	public Company(int id, String username, String password, String email, String name, String address, String zipcode,
			String town, String hashcode) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.address = address;
		this.postcode = zipcode;
		this.town = town;
		this.hashCode = hashcode;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getPasswordNotHashed() {
		return this.passwordNotHashed;
	}

	/**
	 * Used to set the password from information gathered from user input.
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.passwordNotHashed = password;
		this.password = Utility.hashString(password);
	}

	public String getEmail() {
		return this.email;
	}

	public String getName() {
		return this.name;
	}

	public String getAddress() {
		return this.address;
	}

	public String getPostcode() {
		return this.postcode;
	}

	public String getTown() {
		return this.town;
	}

	public String getHashCode() {
		return this.hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public List<String> validate() {
		this.errors.clear();
		this.validateEmail();
		this.validateName();
		this.validateAddress();
		this.validatePostcode();
		this.validateTown();
		return this.errors;
	}

	public List<String> validateUsernameAndPassword() {
		this.errors.clear();
		this.validateUsername();
		this.validatePassword();
		return this.errors;
	}

	public List<String> validateOnlyPassword() {
		this.errors.clear();
		this.validatePassword();
		return this.errors;
	}

	private void validateUsername() {
		Pattern p = Pattern.compile("[\\p{Alpha}\\p{Digit}.-_]{4,64}", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p.matcher(this.username);
		if (!m.matches()) {
			this.errors
					.add("Benutzername muss 4 bis 64 Gross-, Kleinbuchstaben, Zahlen, Punkte, Bindestriche oder Bodenstriche haben");
		}
	}

	private void validatePassword() {
		if (this.passwordNotHashed != null) {
			Pattern p = Pattern.compile("[\\p{Alpha}\\p{Digit}.-_]{8,64}", Pattern.UNICODE_CHARACTER_CLASS);
			Matcher m = p.matcher(this.passwordNotHashed);
			if (!m.matches()) {
				this.errors
						.add("Passwort muss 8 bis 64 Gross-, Kleinbuchstaben, Zahlen, Punkte, Bindestriche oder Bodenstriche haben");
			}
		}
	}

	private void validateEmail() {
		EmailValidator v = EmailValidator.getInstance();
		if (this.email.length() > 100 || !v.isValid(this.email)) {
			this.errors.add("E-Mail ist nicht gültig!");
		}
	}

	private void validateName() {
		Pattern p = Pattern.compile("[\\p{Alpha}\\p{Digit} ]{1,20}", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p.matcher(this.name);
		if (!m.matches()) {
			this.errors.add("Firmenname darf maximal 20 Gross-, Kleinbuchstaben oder Leerzeichen haben");
		}
	}

	private void validateAddress() {
		Pattern p = Pattern.compile("[\\p{Alpha}\\p{Digit} -.]{1,100}", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p.matcher(this.address);
		if (!m.matches()) {
			this.errors
					.add("Adresse darf maximal 100 Gross-, Kleinbuchstaben, Zahlen, Leerzeichen, Bindestriche oder Punkte haben");
		}
	}

	private void validatePostcode() {
		Pattern p = Pattern.compile("[0-9]{4}", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p.matcher(this.postcode);
		if (!m.matches() || !Verifiers.verifyPostcode(this.postcode)) {
			this.errors.add("PLZ ist keine Schweizerische Postleitzahl");
		}
	}

	private void validateTown() {
		Pattern p = Pattern.compile("[\\p{Alpha} -.]{1,100}", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p.matcher(this.town);
		if (!m.matches()) {
			this.errors.add("Stadt darf maximal 100 Gross-, Kleinbuchstaben, Leerzeichen, Bindestriche oder Punkte enthalten");
		}
	}

	@Override
	public String toString() {
		return Company.class.getSimpleName() + " [" + this.getId() + ", " + this.getUsername() + ", " + this.getPassword() + ", "
				+ this.getEmail() + ", " + this.getName() + ", " + this.getAddress() + ", " + this.getPostcode() + ", "
				+ this.getTown() + "]";
	}
}
