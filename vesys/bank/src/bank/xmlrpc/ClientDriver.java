package bank.xmlrpc;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.util.ClientFactory;

import bank.IAccount;
import bank.IBank;
import bank.IBankDriver;
import bank.InactiveException;
import bank.OverdrawException;
import bank.StartClient;

public class ClientDriver implements IBankDriver {
    
    private IBank bank;
    private FlatBank flatBank;

    @Override
    public void connect(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java " + StartClient.class.getName() + " "
                    + ClientDriver.class.getName() + " <server>");
            System.exit(1);
        }
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(args[0]));
        config.setEnabledForExtensions(true);
        //config.setConnectionTimeout(60 * 1000);
        //config.setReplyTimeout(60 * 1000);

        XmlRpcClient client = new XmlRpcClient();

        client.setConfig(config);

        // make a call using dynamic proxy
        ClientFactory factory = new ClientFactory(client);
        bank = new Bank();
        flatBank = (FlatBank) factory.newInstance(FlatBank.class);
    }

    @Override
    public void disconnect() throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public IBank getBank() {
        // TODO Auto-generated method stub
        return bank;
    }
    
    public class Bank implements IBank {

        @Override
        public String createAccount(String owner) throws IOException {
            return flatBank.createAccount(owner);
        }

        @Override
        public boolean closeAccount(String number) throws IOException {
            return flatBank.closeAccount(number);
        }

        @Override
        public Set<String> getAccountNumbers() throws IOException {
            Object[] obj = flatBank.getAccountNumbers();
            Set<String> set = new HashSet<>(obj.length);
            for (Object object : obj) {
                set.add((String)object);
            }
            return set;
        }

        @Override
        public IAccount getAccount(String number) throws IOException {
            if (flatBank.getAccount(number)) {
                return new Account(number);
            }
            return null; 
        }

        @Override
        public void transfer(IAccount a, IAccount b, double amount) throws IOException,
                IllegalArgumentException, OverdrawException, InactiveException {
            flatBank.transfer(a.getNumber(), b.getNumber(), amount);
        }
        
        public class Account implements IAccount {
            
            private String number;
            
            public Account(String number) {
                this.number = number;
            }

            @Override
            public String getNumber() throws IOException {
                return this.number;
            }

            @Override
            public String getOwner() throws IOException {
                return flatBank.getOwner(number);
            }

            @Override
            public boolean isActive() throws IOException {
                return flatBank.isActive(number);
            }

            @Override
            public void deposit(double amount) throws IOException,
                    IllegalArgumentException, InactiveException {
                flatBank.deposit(number, amount);
            }

            @Override
            public void withdraw(double amount) throws IOException,
                    IllegalArgumentException, OverdrawException, InactiveException {
               flatBank.withdraw(number, amount);
            }

            @Override
            public double getBalance() throws IOException {
                return flatBank.getBalance(number);
            }
            
        }
    }

}
