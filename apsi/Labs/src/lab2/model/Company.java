package lab2.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lab2.helpers.Utility;

/*
 * TODO: http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
 * http://www.unicode.org/reports/tr18/
 */
public class Company {

	private final List<String> errors = new ArrayList<>();

	private int id;
	private String username;
	private String password;
	private final String email;
	private final String name;
	private final String address;
	private final String postcode;
	private final String town;
	private String hashCode;

	public Company(String email, String name, String address, String zipcode, String town) {
		this(0, null, null, email, name, address, zipcode, town, null);
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

	/**
	 * Used to set the password from information gathered from user input.
	 * @param password
	 * @throws NoSuchAlgorithmException
	 */
	public void setPassword(String password) throws NoSuchAlgorithmException {
		this.password = password;
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
		this.validateEmail();
		this.validateName();
		this.validateAddress();
		this.validatePostcode();
		this.validateTown();
		return this.errors;
	}
	
	public List<String> validateUsernameAndPassword() {
		List<String> errs = new ArrayList<String>();
		String errUser = validateUsername();
		String errPass = validatePassword();
		if (errUser != null) errs.add(errUser);
		if (errPass != null) errs.add(errPass);
		return errs;
	}

	private String validateUsername() {
		Pattern p = Pattern.compile("[\\p{Alpha}\\p{Digit}.-_]{4,64}", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p.matcher(this.username);
		if (!m.matches()) {
			return "Es sind 4 bis 64 Gross-, Kleinbuchstaben, Zahlen, Punkte, Bindestriche oder Bodenstriche erlaubt";
		} else {
			return null;
		}
	}

	private String validatePassword() {
		Pattern p = Pattern.compile("[\\p{Alpha}\\p{Digit}.-_]{8,64}", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p.matcher(this.password);
		if (!m.matches()) {
			return "Es sind 8 bis 64 Gross-, Kleinbuchstaben, Zahlen, Punkte, Bindestriche oder Bodenstriche erlaubt";
		} else {
			return null;
		}
	}

	private void validateEmail() {
		// TODO: validate, if invalid add error message to error
	}

	private void validateName() {
		Pattern p = Pattern.compile("[\\p{Alpha}\\p{Digit} ]{0,20}", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p.matcher(this.name);
		if (!m.matches()) {
			this.errors.add("Es sind maximal 20 Gross-, Kleinbuchstaben oder Leerzeichen erlaubt");
		}
	}

	private void validateAddress() {
		Pattern p = Pattern.compile("[\\p{Alpha}\\p{Digit} -.]{0,100}", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p.matcher(this.address);
		if (!m.matches()) {
			this.errors.add("Es sind maximal 100 Gross-, Kleinbuchstaben, Zahlen, Leerzeichen, Bindestriche oder Punkte erlaubt");
		}
	}

	private void validatePostcode() {
		Pattern p = Pattern.compile("[0-9]{4}", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p.matcher(this.postcode);
		if (!m.matches()) {
			this.errors.add("Es ist nur eine gültige schweizer PLZ erlaubt");
		}
	}

	private void validateTown() {
		Pattern p = Pattern.compile("[\\p{Alpha} -.]{0,100}", Pattern.UNICODE_CHARACTER_CLASS);
		Matcher m = p.matcher(this.town);
		if (!m.matches()) {
			this.errors.add("Es sind maximal 100 Gross-, Kleinbuchstaben, Leerzeichen, Bindestriche oder Punkte erlaubt");
		}
	}

	@Override
	public String toString() {
		return Company.class.getSimpleName() + " [" + this.getId() + ", " + this.getUsername() + ", " + this.getPassword() + ", "
				+ this.getEmail() + ", " + this.getName() + ", " + this.getAddress() + ", " + this.getPostcode() + ", "
				+ this.getTown() + "]";
	}
}
