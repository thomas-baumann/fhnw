package lab2;

import java.io.IOException;
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

	public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("GET".equals(request.getMethod())) {
			request.getRequestDispatcher(REGISTER).forward(request,	response);
		} else if ("POST".equals(request.getMethod())
				|| "PUT".equals(request.getMethod())) {
			// TODO implement this
		}
	}

	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> errors = new ArrayList<String>();
		if ("GET".equals(request.getMethod())) {
			request.getRequestDispatcher(LOGIN).forward(request, response);
		} else if ("POST".equals(request.getMethod())
				|| "PUT".equals(request.getMethod())) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
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
						c.getPassword() != null && password.equals(c.getPassword())) {
					if (request.getSession().getAttribute("userId") == null) {
						request.getSession().setAttribute("userId", c.getId()); // TODO if enough time: implement something with that information (could be valuable)
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

	public void passwordReset(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("GET".equals(request.getMethod())) {
			request.getRequestDispatcher(PASSWORDRESET).forward(request, response);
		} else if ("POST".equals(request.getMethod())
				|| "PUT".equals(request.getMethod())) {
			// TODO implement this
		}
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("POST".equals(request.getMethod())) {
			request.getSession().setAttribute("userId", null);
			request.getRequestDispatcher(INDEX).forward(request, response);
		}
	}

}
