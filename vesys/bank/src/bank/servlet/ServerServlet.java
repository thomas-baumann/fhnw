package bank.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.IAccount;
import bank.IBank;
import bank.communication.answer.IAnswer;
import bank.communication.request.IRequest;
import bank.local.Bank;

/**
 * This class provides an implementation of a {@link HttpServlet} to handle post requests
 * with a bank and returns all active bank accounts for a get request.
 * 
 * @see HttpServlet
 * @author Thomas Baumann
 * @version 1.0
 */
public class ServerServlet extends HttpServlet {

    private IBank bank = new Bank();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println(" <body>");
        out.println("  <h1>Bankkonten</h1>");
        Set<String> numbers = this.bank.getAccountNumbers();
        if (numbers.size() > 0) {
            out.println("  <table border=\"1px\">");
            for (String number : numbers) {
                IAccount account = this.bank.getAccount(number);
                out.println("   <tr>");
                out.println("    <td width=\"50px\">" + account.getNumber() + "</td>");
                out.println("    <td width=\"200px\">" + account.getOwner() + "</td>");
                out.println("    <td width=\"50px\">" + account.getBalance() + "</td>");
                out.println("   </tr>");
            }
            out.println("  </table>");
        } else {
            out.println("  <p>Es sind keine Konten vorhanden.</p");
        }
        out.println(" </body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (request.getContentType().equals("application/x-java-serialized-object")) {
            try {
                ObjectInputStream oin = new ObjectInputStream(request.getInputStream());
                Object o = oin.readObject();
                IAnswer<?> answer = ((IRequest) o).handleRequest(this.bank);

                response.setContentType("application/x-java-serialized-object");
                ObjectOutputStream oout = new ObjectOutputStream(
                        response.getOutputStream());
                oout.writeObject(answer);
            } catch (Exception e) {
                System.err.println("invalid object arrived");
                e.printStackTrace();
            }
        } else {
            this.doGet(request, response);
        }
    }

}
