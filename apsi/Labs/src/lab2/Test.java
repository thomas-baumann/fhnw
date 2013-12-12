package lab2;

import java.sql.SQLException;

public class Test {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// CompanyDAO dao = new CompanyDAO();
		// Company c = new Company("test", "dfasd", "hui", "FHNW", "strasse 4", "2424", "Brugg");
		// dao.saveOrUpdaetCompany(c);
		// System.out.println(dao.getCompanyByUsername("test"));

		System.out.println(Verifiers.verifyPostcode("9999"));
	}
}
