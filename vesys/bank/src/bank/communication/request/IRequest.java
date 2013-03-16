package bank.communication.request;

import java.io.Serializable;

import bank.IBank;
import bank.communication.answer.IAnswer;

/**
 * This interface must be used as request object for the socket communication from the
 * client to the server.
 * 
 * @author Thomas Baumann
 * @version 1.1
 */
public interface IRequest extends Serializable {

    /**
     * Handles the request with the specified bank.
     * 
     * @param b Bank to hande the request
     * @return answer object to send back
     */
    public IAnswer<?> handleRequest(IBank b);

}
