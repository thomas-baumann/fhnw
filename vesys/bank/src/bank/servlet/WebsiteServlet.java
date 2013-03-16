package bank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.IAccount;
import bank.IBank;
import bank.local.Bank;

/**
 * This class provides an implementation of a {@link HttpServlet} to handle get and post
 * requests which shows all active accounts.
 * 
 * @see HttpServlet
 * @author Thomas Baumann
 * @version 1.0
 */
public class WebsiteServlet extends HttpServlet {

    private final String PASSWORD = "bank";
    private IBank bank;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println(" <head>");
        out.println("  <title>Bank Server</title>");
        out.println(" </head>");
        out.println(" <body>");
        out.println("  <h1>Bankkonten</h1>");
        String password = request.getParameter("password");
        if (this.PASSWORD.equals(password)) {
            if (this.bank != null) {
                this.showAccounts(out);
            } else {
                out.println("Die Konten k&ouml;nnen nicht abgerufen werden.");
            }
        } else {
            out.println("   <form name=\"form\" action=\"" + request.getRequestURI()
                    + "\" method=\"post\">");
            out.println("    <label for=\"password\">Passwort:</label><br>");
            out.println("    <input type=\"password\" id=\"password\" name=\"password\"><br>");
            out.println("    <br>");
            out.println("    <input type=\"submit\" value=\"Anmelden\">");
            out.println("   </form>");
        }
        out.println(" </body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        this.doGet(request, response);
    }

    /**
     * Prints all accounts to the specified PrintWriter in a table.
     * 
     * @param out PrintWriter to print accounts
     */
    private void showAccounts(PrintWriter out) {
        try {
            Set<String> numbers = this.bank.getAccountNumbers();
            if (numbers.size() > 0) {
                out.println("  <table border=\"1px\">");
                for (String number : numbers) {
                    IAccount account = this.bank.getAccount(number);
                    out.println("   <tr>");
                    out.println("    <td width=\"50px\">" + account.getNumber() + "</td>");
                    out.println("    <td width=\"200px\">" + account.getOwner() + "</td>");
                    out.println("    <td width=\"100px\">" + account.getBalance()
                            + "</td>");
                    out.println("   </tr>");
                }
                out.println("  </table>");
            } else {
                out.println("  <p>Es sind keine Konten vorhanden.</p");
            }
        } catch (IOException e) {
            // ignore, should not happen
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.bank = (Bank) this.getServletContext().getAttribute("bank");
    }
}