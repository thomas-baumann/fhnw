package lab2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lab2.model.Company;

public final class CompanyDAO {

	private Connection connection;

	public CompanyDAO(Connection connection) {
		this.connection = connection;
	}

	public Company getCompanyByUsername(String username) throws SQLException {
		PreparedStatement pStatement = this.connection.prepareStatement("SELECT * FROM `companies` WHERE `username`=?");
		pStatement.setString(1, username);

		ResultSet rs = pStatement.executeQuery();

		return this.createCompany(rs);
	}

	public void saveOrUpdateCompany(Company company) throws SQLException {
		PreparedStatement pStatement;
		if (company.getId() == 0) {
			pStatement = this.connection
					.prepareStatement("INSERT INTO `companies` (`username`, `password`, `email`, `name`, `address`, `postcode`, `town`, `hashcode`) VALUES (?,?,?,?,?,?,?,?)");
		} else {
			pStatement = this.connection
					.prepareStatement("UPDATE `companies` SET `username`=?,`password`=?,`email`=?,`name`=?,`address`=?,`postcode`=?,`town`=?,`hashcode`=? WHERE id = ?");
			pStatement.setInt(9, company.getId());
		}
		pStatement.setString(1, company.getUsername());
		pStatement.setString(2, company.getPassword());
		pStatement.setString(3, company.getEmail());
		pStatement.setString(4, company.getName());
		pStatement.setString(5, company.getAddress());
		pStatement.setString(6, company.getPostcode());
		pStatement.setString(7, company.getTown());
		pStatement.setString(8, company.getHashCode());

		pStatement.executeUpdate();
	}

	private Company createCompany(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return new Company(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"),
					rs.getString("name"), rs.getString("address"), rs.getString("postcode"), rs.getString("town"),
					rs.getString("hashcode"));
		} else {
			return null;
		}
	}

	public Company getCompanyById(int id) throws SQLException {
		PreparedStatement pStatement = this.connection.prepareStatement("SELECT * FROM `companies` WHERE `id`=?");
		pStatement.setInt(1, id);

		ResultSet rs = pStatement.executeQuery();

		return this.createCompany(rs);
	}

	public Company getCompanyByPassword(String pass) throws SQLException {
		PreparedStatement pStatement = this.connection.prepareStatement("SELECT * FROM `companies` WHERE `password`=?");
		pStatement.setString(1, pass);

		ResultSet rs = pStatement.executeQuery();

		return this.createCompany(rs);
	}
}
