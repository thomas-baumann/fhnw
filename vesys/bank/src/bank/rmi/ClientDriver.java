package bank.rmi;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import bank.IBank;
import bank.IBankDriver;
import bank.IRemoteBank;
import bank.StartClient;

public class ClientDriver implements IBankDriver {

    private IRemoteBank bank;

    @Override
    public void connect(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java " + StartClient.class.getName() + " "
                    + ClientDriver.class.getName() + " <host> <port>");
            System.exit(1);
        }
        try {
            bank = (IRemoteBank)Naming.lookup("rmi://"+args[0]+":"+args[1]+"/Bank");
        } catch (NotBoundException e) {
            System.out.println("Bank can not be found");
            System.exit(1);
        }
    }

    @Override
    public void disconnect() throws IOException {
        bank = null;
    }


    @Override
    public IBank getBank() {
        return bank;
    }

}
