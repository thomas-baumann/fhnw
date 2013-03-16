package bank.communication.answer;

import java.io.IOException;
import java.io.Serializable;

import bank.InactiveException;
import bank.OverdrawException;

/**
 * This interface must be used as answer object for the socket communication from the
 * server to the client.
 * 
 * @author Thomas Baumann
 * @version 1.0
 * @param <T>
 */
public interface IAnswer<T> extends Serializable {

    /**
     * Returns an object or throws an exception.
     * 
     * @return Returns the answer
     * @throws IllegalArgumentException When answer is an IllegalArgumentException
     * @throws IOException When an IO problem occurs
     * @throws OverdrawException When answer is an OverdrawException
     * @throws InactiveException When answer is an InactiveException
     */
    public T getData() throws IllegalArgumentException, IOException, OverdrawException,
            InactiveException;

}
