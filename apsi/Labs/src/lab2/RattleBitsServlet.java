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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		switch (request.getParameter("page")) {
		case "login":
			controller.login(request, response);
			break;
		case "register":
			controller.register(request, response);
			break;
		case "password-reset":
			controller.passwordReset(request, response);
			break;
		case "main":
			controller.main(request, response);
			break;
		default:
			controller.index(request, response);
			break;
		}
		;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("register") != null) {
			controller.register(request, response);
		} else if (request.getParameter("login") != null) {
			controller.login(request, response);
		} else if (request.getParameter("password_reset") != null) {
			controller.passwordReset(request, response);
		} else if (request.getParameter("logout") != null) {
			controller.logout(request, response);
		}
	}

}
