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
        config.setEnabledForExceptions(true);
        config.setContentLengthOptional(true);

        XmlRpcClient client = new XmlRpcClient();

        client.setConfig(config);

        ClientFactory factory = new ClientFactory(client);
        this.bank = new Bank();
        this.flatBank = (FlatBank) factory.newInstance(FlatBank.class);
    }

    @Override
    public void disconnect() throws IOException {
        this.bank = null;
        this.flatBank = null;
    }

    @Override
    public IBank getBank() {
        return this.bank;
    }

    public class Bank implements IBank {

        @Override
        public String createAccount(String owner) throws IOException {
            return ClientDriver.this.flatBank.createAccount(owner);
        }

        @Override
        public boolean closeAccount(String number) throws IOException {
            return ClientDriver.this.flatBank.closeAccount(number);
        }

        @Override
        public Set<String> getAccountNumbers() throws IOException {
            Object[] obj = ClientDriver.this.flatBank.getAccountNumbers();
            Set<String> set = new HashSet<>(obj.length);
            for (Object object : obj) {
                set.add((String) object);
            }
            return set;
        }

        @Override
        public IAccount getAccount(String number) throws IOException {
            if (ClientDriver.this.flatBank.getAccount(number)) {
                return new Account(number);
            }
            return null;
        }

        @Override
        public void transfer(IAccount a, IAccount b, double amount) throws IOException,
                IllegalArgumentException, OverdrawException, InactiveException {
            ClientDriver.this.flatBank.transfer(a.getNumber(), b.getNumber(), amount);
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
                return ClientDriver.this.flatBank.getOwner(this.number);
            }

            @Override
            public boolean isActive() throws IOException {
                return ClientDriver.this.flatBank.isActive(this.number);
            }

            @Override
            public void deposit(double amount) throws IOException,
                    IllegalArgumentException, InactiveException {
                ClientDriver.this.flatBank.deposit(this.number, amount);
            }

            @Override
            public void withdraw(double amount) throws IOException,
                    IllegalArgumentException, OverdrawException, InactiveException {
                ClientDriver.this.flatBank.withdraw(this.number, amount);
            }

            @Override
            public double getBalance() throws IOException {
                return ClientDriver.this.flatBank.getBalance(this.number);
            }
        }
    }
}
