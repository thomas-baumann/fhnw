package lab2;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.StringUtils;

import lab2.model.Company;
import lab2.database.CompanyDAO;
import lab2.helpers.Utility;

public class Controller {

	private final static String INDEX = "/index.jsp";
	private final static String LOGIN = "/login.jsp";
	private final static String MAIN = "/main.jsp";
	private final static String PASSWORDRESET = "/password_reset.jsp";
	private final static String REGISTER = "/register.jsp";
	
	private CompanyDAO companyDAO;
	
	public Controller() throws SQLException, ClassNotFoundException {
			companyDAO = new CompanyDAO();
	}
	
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("GET".equals(request.getMethod())) {
			request.getRequestDispatcher(INDEX).forward(request, response);
		}
	}

	public void main(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("GET".equals(request.getMethod())) {
			request.getRequestDispatcher(MAIN).forward(request, response);
		}
	}
	
	public void registerGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("GET".equals(request.getMethod())) {
			request.getRequestDispatcher(REGISTER).forward(request,	response);
		}
	}

	public void registerPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
		if ("POST".equals(request.getMethod())) {
			String name = request.getParameter("companyname");
			String address = request.getParameter("address");
			String zipcode = request.getParameter("plz");
			String town = request.getParameter("city");
			String email = request.getParameter("mail");
			Company c = new Company(email, name, address, zipcode, town);
			List<String> errors = c.validate();
			if (errors.size() == 0) {
				int counter = 1;
				String password = null;
				while (companyDAO.getCompanyByUsername(name + counter) != null) {
					counter++;
				}
				String username = name + counter;
				c.setUsername(username);
				password = Utility.generateRandomString(32);
//				errors.add(password); // TODO debugging
				c.setPassword(Utility.hashString(password));
			} else {
				errors.add("Benutzereingaben sind nicht korrekt.");
			}
			if (errors.size() == 1) {
				companyDAO.saveOrUpdateCompany(c);
			}
			if (errors.size() > 0) {
				request.setAttribute("errors", errors);
				request.getRequestDispatcher(REGISTER).forward(request, response);
			} else {
				errors.add("Sie haben eine E-Mail mit Ihrem Benutzername und Passwort erhalten.\nBitte loggen Sie sich mit diesem nun ein.");
				request.setAttribute("errors", errors);
				request.getRequestDispatcher(LOGIN).forward(request, response);
			}
		}
	}
	
	public void loginGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		if ("GET".equals(request.getMethod())) {
			if (request.getSession().getAttribute("userId") != null && request.getSession().getAttribute("hashCode") != null) {
				String hashCode = (String) request.getSession().getAttribute("hashCode");
				int userId = (int) request.getSession().getAttribute("userId");
				Company c = companyDAO.getCompanyById(userId);
				if (c != null && c.getHashCode() != null && hashCode.equals(c.getHashCode())) {
					request.getRequestDispatcher(MAIN).forward(request, response);
				}
			} else {
				request.getRequestDispatcher(LOGIN).forward(request, response);
			}
		}
	}

	public void loginPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		List<String> errors = new ArrayList<String>();
		if ("POST".equals(request.getMethod())) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if (username == null || "".equals(username)) {
				errors.add("Benutzername fehlt!");
			}
			if (password == null || "".equals(password)) {
				errors.add("Passwort fehlt!");
			}
			String hashedPassword = Utility.hashString(password);
			Company c = null;
			c = companyDAO.getCompanyByUsername(username);
			if (c == null && errors.size() == 0) {
				errors.add("Benutzername oder Passwort nicht korrekt.");
			}
			if (errors.size() == 0) {
				if (c.getUsername() != null && username.equals(c.getUsername()) && 
						c.getPassword() != null && hashedPassword.equals(c.getPassword())) {
					if (request.getSession().getAttribute("userId") == null) {
						String hash = Utility.generateHashedString();
						request.getSession().setAttribute("userId", c.getId());
						request.getSession().setAttribute("hashCode", hash);
						c.setHashCode(hash);
						companyDAO.saveOrUpdateCompany(c);
					}
					request.getRequestDispatcher(MAIN).forward(request, response);
				} else {
					errors.add("Benutzername oder Passwort nicht korrekt.");
					request.setAttribute("errors", errors);
					request.getRequestDispatcher(LOGIN).forward(request, response);
				}
			} else {
				request.setAttribute("errors", errors);
				request.getRequestDispatcher(LOGIN).forward(request, response);
			}
		}
	}

	public void passwordResetGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("GET".equals(request.getMethod())) {
			request.getRequestDispatcher(PASSWORDRESET).forward(request, response);
		}
	}
	
	public void passwordResetPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, NoSuchAlgorithmException {
		if ("POST".equals(request.getMethod())) {
			List<String> errors = new ArrayList<String>();
			List<String> success = new ArrayList<String>();
			String oldPass = request.getParameter("oldpass");
			String hashedOldPass = Utility.hashString(oldPass);
			String newPass = request.getParameter("newpass");
			String newPassConf = request.getParameter("newpassconf");
			if (newPass == null) {
				errors.add("Neues Passwort darf nicht leer sein.");
			}
			if (errors.size() == 0 && !newPass.equals(newPassConf)) {
				errors.add("Bestätigung stimmt nicht mit dem Passwort überein.");
			}

			Company c = null;
			if (errors.size() == 0) {
				if (checkSession(request)) {
					int id = (int) request.getSession().getAttribute("userId");
					c = companyDAO.getCompanyById(id);
					if (errors.size() == 0 && !c.getPassword().equals(hashedOldPass)) {
						errors.add("Passwort stimmt nicht." + hashedOldPass);
					}
				} else {
					errors.add("Identifikationsfehler!");
					request.getSession().setAttribute("userId", null);
					request.getSession().setAttribute("hashCode", null);
				}
			}
			
			if (errors.size() == 0) {
				c.setPassword(newPass);
				errors.addAll(c.validateUsernameAndPassword());
				c.setPassword(Utility.hashString(newPass));
				if (errors.size() == 0){
					companyDAO.saveOrUpdateCompany(c);
				}
			}
			if (errors.size() > 0) {
				request.setAttribute("errors", errors);
				request.getRequestDispatcher(PASSWORDRESET).forward(request, response);
			} else {
				success.add("Änderung war erfolgreich.");
				request.setAttribute("success", success);
				request.getRequestDispatcher(MAIN).forward(request, response);
			}
		}
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		if ("GET".equals(request.getMethod())) {
			if (request.getSession().getAttribute("userId") != null) {
				Company c = companyDAO.getCompanyById((int)request.getSession().getAttribute("userId"));
				c.setHashCode(null);
				companyDAO.saveOrUpdateCompany(c);
			}
			request.getSession().setAttribute("userId", null);
			request.getSession().setAttribute("hashCode", null);
			request.getRequestDispatcher(INDEX).forward(request, response);
		}
	}
	
	private boolean checkSession(HttpServletRequest request) throws SQLException {
		if (request.getSession().getAttribute("userId") == null) return false;
		int userId = (int) request.getSession().getAttribute("userId");
		
		if (request.getSession().getAttribute("hashCode") == null) return false;
		String hash = (String) request.getSession().getAttribute("hashCode");
		
		Company c = companyDAO.getCompanyById(userId);
		if (c == null) return false;
		
		if (!hash.equals(c.getHashCode())) return false;
		return true;
	}
	
}
