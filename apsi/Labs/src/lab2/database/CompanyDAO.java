package lab2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lab2.model.Company;

public class CompanyDAO {

	private Connection connection;

	public CompanyDAO() throws SQLException {
		this.connection = DriverManager.getConnection("jdbc:mysql://localhost/apsi_lab2", "apsi", "0P]wEvoeSwEz+lYg");
	}

	public Company getCompanyByUsername(String username) throws SQLException {
		PreparedStatement pStatement = this.connection.prepareStatement("SELECT * FROM `companies` WHERE `username`=?");
		pStatement.setString(1, username);

		ResultSet rs = pStatement.executeQuery();

		return this.createCompany(rs);
	}

	public void saveOrUpdaetCompany(Company company) throws SQLException {
		PreparedStatement pStatement;
		if (company.getId() == 0) {
			pStatement = this.connection
					.prepareStatement("INSERT INTO `companies` (`username`, `password`, `email`, `name`, `address`, `postcode`, `town`) VALUES (?,?,?,?,?,?,?)");
		} else {
			pStatement = this.connection
					.prepareStatement("UPDATE `companies` SET `username`=?,`password`=?,`email`=?,`name`=?,`address`=?,`postcode`=?,`town`=? WHERE id = ?");
			pStatement.setInt(8, company.getId());
		}
		pStatement.setString(1, company.getUsername());
		pStatement.setString(2, company.getPassword());
		pStatement.setString(3, company.getEmail());
		pStatement.setString(4, company.getName());
		pStatement.setString(5, company.getAddress());
		pStatement.setInt(6, company.getPostcode());
		pStatement.setString(7, company.getTown());

		pStatement.executeUpdate();
	}

	private Company createCompany(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return new Company(rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("name"),
					rs.getString("address"), rs.getInt("postcode"), rs.getString("town"));
		} else {
			return null;
		}
	}
}
