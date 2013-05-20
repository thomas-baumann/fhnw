package bank;

import java.rmi.Remote;

/**
 * This interface extends the IBank Interface with the marker interface java.rmi.Remote
 * 
 * @see IBank, Remote
 * @author Thomas Baumann
 * @version 1.0
 */
public interface IRemoteBank extends IBank, Remote {

}
