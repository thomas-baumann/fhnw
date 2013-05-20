package bank.rmi;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import bank.IRemoteBank;
import bank.IServerDriver;
import bank.StartServer;
import bank.local.Bank;

public class ServerDriver implements IServerDriver {

    private IRemoteBank bank = new Bank();

    @Override
    public void start(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java " + StartServer.class.getName() + " "
                    + ServerDriver.class.getName() + " <host> <portnumber>");
            System.exit(1);
        }
        String host = args[0];
        int port = 0;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Port must be a number");
            System.exit(1);
        }

        try {
            LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            System.out.println("registry could not be exported");
            System.exit(1);
        }

        UnicastRemoteObject.exportObject(this.bank, 0);

        Naming.rebind("rmi://" + host + ":" + port + "/Bank", this.bank);
    }

}
