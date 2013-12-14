package lab2;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RattleBitsFront")
public class RattleBitsServlet extends HttpServlet {

	private static final long serialVersionUID = -4099191990249828904L;
	private Controller controller;

	public RattleBitsServlet() throws SQLException, ClassNotFoundException {
		super();
		this.controller = new Controller();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			String page = request.getParameter("page");
			if (page == null) {
				page = "";
			}
			switch (page) {
				case "login":
					this.controller.loginGet(request, response);
					break;
				case "register":
					this.controller.registerGet(request, response);
					break;
				case "password_reset":
					this.controller.passwordResetGet(request, response);
					break;
				case "main":
					this.controller.mainGet(request, response);
					break;
				case "logout":
					this.controller.logoutGet(request, response);
					break;
				default:
					this.controller.indexGet(request, response);
					break;
			}
		} catch (Exception e) {
			response.sendError(500);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			switch (request.getParameter("page")) {
				case "login":
					this.controller.loginPost(request, response);
					break;
				case "register":
					this.controller.registerPost(request, response);
					break;
				case "password_reset":
					this.controller.passwordResetPost(request, response);
					break;
				default:
					this.controller.indexGet(request, response);
					break;
			}
		} catch (Exception e) {
			response.sendError(500);
		}
	}

}
