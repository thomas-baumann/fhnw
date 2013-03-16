package bank.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bank.IBank;
import bank.communication.answer.IAnswer;
import bank.communication.request.IRequest;
import bank.local.Bank;

/**
 * This class provides an implementation of a {@link HttpServlet} to handle post requests
 * from the bank GUI. The content type of the post request must be set to
 * "application/x-java-serialized-object" otherwise a 400 error will be send. A
 * 
 * @see HttpServlet
 * @author Thomas Baumann
 * @version 1.0
 */
public class ServerServlet extends HttpServlet {
    private final String CONTENT_TYPE = "application/x-java-serialized-object";
    private IBank bank = new Bank();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (this.CONTENT_TYPE.equals(request.getContentType())) {
            try {
                ObjectInputStream oin = new ObjectInputStream(request.getInputStream());
                Object o = oin.readObject();
                IAnswer<?> answer = ((IRequest) o).handleRequest(this.bank);

                response.setContentType("application/x-java-serialized-object");
                ObjectOutputStream oout = new ObjectOutputStream(
                        response.getOutputStream());
                oout.writeObject(answer);
            } catch (Exception e) {
                response.sendError(400, "Bad Request");
            }
        } else {
            response.sendError(400, "Bad Request");
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.getServletContext().setAttribute("bank", this.bank);
    }
}
