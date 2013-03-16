package bank;

import java.io.IOException;

/**
 * The ServerDriver interface is used to access a particular bank server. The main program
 * calls start to start the server.
 * 
 * @author Thomas Baumann
 * @version 1.0
 */
public interface IServerDriver {

    /**
     * Starts an implementation of a bank server. Parameters which designate e.g. the port
     * of the server and possibly other arguments may be passed.
     * 
     * @param args array of implementation specific arguments
     * @throws IOException if a communication problem occurs
     */
    public void start(String[] args) throws IOException;

}
