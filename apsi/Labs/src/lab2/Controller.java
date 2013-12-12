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

import lab2.model.Company;
import lab2.database.CompanyDAO;

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

	public void registerPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("POST".equals(request.getMethod())) {
			String name = request.getParameter("companyname");
			String address = request.getParameter("address");
			String zipcode = request.getParameter("plz");
			String town = request.getParameter("city");
			String email = request.getParameter("mail");
			Company c = new Company(email, name, address, zipcode, town);
			List<String> errors = new ArrayList<>();
			String[] valErr = c.validate();
			for (String err : valErr) {
				errors.add(err);
			}
			if (errors.size() == 0) {
				String username = "blub"; // TODO generate these
				String password = "blub";
				c.setUsername(username);
				try {
					c.setPassword(password);
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException(e.getMessage());
				}
				List<String> errPassUser = c.validateUsernameAndPassword();
				errors.addAll(errPassUser);
			} else {
				errors.add("Benutzereingaben sind nicht korrekt.");
			}
			if (errors.size() == 0) {
				try {
					companyDAO.saveOrUpdaetCompany(c);
				} catch (SQLException e) {
					errors.add("Verbindung zur Datenbank fehlgeschlagen.");
				}
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
	
	public void loginGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("GET".equals(request.getMethod())) {
			request.getRequestDispatcher(LOGIN).forward(request, response);
		}
	}

	public void loginPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> errors = new ArrayList<String>();
		if ("POST".equals(request.getMethod())) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String hashedPassword = hashString(password);
			if (username == null || "".equals(username)) {
				errors.add("Benutzername fehlt!");
			}
			if (password == null || "".equals(password)) {
				errors.add("Passwort fehlt!");
			}
			Company c = null;
			try {
				c = companyDAO.getCompanyByUsername(username);
			} catch (SQLException e) {
				errors.add("Verbindung zur Datenbank fehlgeschlagen.");
			}
			if (c == null && errors.size() == 0) {
				errors.add("Benutzername oder Passwort nicht korrekt.");
			}
			if (errors.size() == 0) {
				if (c.getUsername() != null && username.equals(c.getUsername()) && 
						c.getPassword() != null && hashedPassword.equals(c.getPassword())) {
					if (request.getSession().getAttribute("userId") == null) {
						request.getSession().setAttribute("userId", c.getId()); // TODO Hash this. if enough time: implement something with that information (could be valuable)
						request.getSession().setAttribute("hashCode", hashString(c.toString())); // TODO save this in database
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
	
	public void passwordResetPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("POST".equals(request.getMethod())) {
			List<String> errors = new ArrayList<String>();
			String oldPass = request.getParameter("oldpass");
			String hashedOldPass = hashString(oldPass);
			String newPass = request.getParameter("newpass");
			String newPassConf = request.getParameter("newpassconf");
			if (newPass == null) errors.add("Neues Passwort darf nicht leer sein.");
			if (errors.size() == 0 && !newPass.equals(newPassConf)) errors.add("Bestätigung stimmt nicht mit dem Passwort überein.");

			Company c = null;
			if (errors.size() == 0) {
				int id = (int) request.getSession().getAttribute("userId");
				// TODO check for hash in database
				try {
					c = companyDAO.getCompanyById(id);
				} catch (SQLException e) {
					errors.add("Verbindung zur Datenbank fehlgeschlagen.");
				}
				if (errors.size() == 0 && !c.getPassword().equals(hashedOldPass)) {
					errors.add("Passwort stimmt nicht." + hashedOldPass);
				}
			}
			
			if (errors.size() == 0) {
				try {
					c.setPassword(hashString(newPass));
				} catch (NoSuchAlgorithmException e) {
					// TODO proof ignore
				}
				try {
					companyDAO.saveOrUpdaetCompany(c);
				} catch (SQLException e) {
					errors.add("Verbindung zur Datenbank fehlgeschlagen.");
				}
			}
			if (errors.size() > 0) {
				request.setAttribute("errors", errors);
				request.getRequestDispatcher(PASSWORDRESET).forward(request, response);
			} else { //TODO make a success
				errors.add("Änderung war erfolgreich.");
				request.setAttribute("errors", errors);
				request.getRequestDispatcher(PASSWORDRESET).forward(request, response);
			}
		}
	}

	public void logoutPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("POST".equals(request.getMethod())) {
			request.getSession().setAttribute("userId", null);
			request.getRequestDispatcher(INDEX).forward(request, response);
		}
	}
	
	private String hashString(String in) {
		try {
			return new String(MessageDigest.getInstance("SHA-256").digest(in.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
}
