package lab2;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/RattleBitsFront")
public class RattleBitsServlet extends HttpServlet {

	private static final long serialVersionUID = -4099191990249828904L;
	private Controller controller;

	public RattleBitsServlet() throws SQLException, ClassNotFoundException {
		super();
		controller = new Controller();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			switch (request.getParameter("page")) {
			case "login":
				controller.loginGet(request, response);
				break;
			case "register":
				controller.registerGet(request, response);
				break;
			case "password_reset":
				controller.passwordResetGet(request, response);
				break;
			case "main":
				controller.main(request, response);
				break;
			case "logout":
				controller.logout(request, response);
				break;
			default:
				controller.index(request, response);
				break;
			}
			;
		} catch (Exception e) {
			response.sendError(500);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if (request.getParameter("register") != null) {
				controller.registerPost(request, response);
			} else if (request.getParameter("login") != null) {
				controller.loginPost(request, response);
			} else if (request.getParameter("password_reset") != null) {
				controller.passwordResetPost(request, response);
			}
		} catch (Exception e) {
			response.sendError(500);
		}
	}

}
