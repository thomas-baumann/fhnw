package bank;

import java.rmi.Remote;

/**
 * This interface extends the IAccount Interface with the marker interface java.rmi.Remote
 * 
 * @see IAccount, Remote
 * @author Thomas Baumann
 * @version 1.0
 */
public interface IRemoteAccount extends IAccount, Remote {

}
