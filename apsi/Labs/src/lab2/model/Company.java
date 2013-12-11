package lab2.model;

import java.util.ArrayList;
import java.util.List;

public class Company {

	private final List<String> errors = new ArrayList<>();

	private int id;
	private String username;
	private String password;
	private final String email;
	private final String name;
	private final String address;
	private final int postcode;
	private final String town;

	public Company(String username, String password, String email, String name, String address, int zipcode, String town) {
		this(0, username, password, email, name, address, zipcode, town);
	}

	public Company(int id, String username, String password, String email, String name, String address, int zipcode, String town) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.name = name;
		this.address = address;
		this.postcode = zipcode;
		this.town = town;
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

	public void setPassword(String password) {
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

	public int getPostcode() {
		return this.postcode;
	}

	public String getTown() {
		return this.town;
	}

	public String[] validate() {
		this.validateUsername();
		this.validatePassword();
		this.validateEmail();
		this.validateName();
		this.validateAddress();
		this.validatePostcode();
		this.validateTown();
		return this.errors.toArray(new String[this.errors.size()]);
	}

	private void validateUsername() {
		// TODO: validate, if invalid add error message to error
	}

	private void validatePassword() {
		// TODO: validate, if invalid add error message to error
	}

	private void validateEmail() {
		// TODO: validate, if invalid add error message to error
	}

	private void validateName() {
		if (this.name.trim().isEmpty()) {
			this.errors.add("Firma eingeben");
		} else if (this.name.trim().length() > 20) {
			this.errors.add("Firma zu lang (max 20 Zeichen)");
		} else if (!this.name.matches("[èéÈÉäöüÄÖÜß\\w\\s]+")) {
			this.errors.add("Ungültige Zeichen in der Firmennamen");
		}
	}

	private void validateAddress() {
		// TODO: validate, if invalid add error message to error
	}

	private void validatePostcode() {
		// TODO: validate, if invalid add error message to error
	}

	private void validateTown() {
		// TODO: validate, if invalid add error message to error
	}

	@Override
	public String toString() {
		return Company.class.getSimpleName() + " [" + this.getId() + ", " + this.getUsername() + ", " + this.getPassword() + ", "
				+ this.getEmail() + ", " + this.getName() + ", " + this.getAddress() + ", " + this.getPostcode() + ", "
				+ this.getTown() + "]";
	}
}
