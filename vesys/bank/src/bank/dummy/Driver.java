package bank.dummy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bank.IAccount;
import bank.IBank;
import bank.IBankDriver;

/**
 * This class implements a dummy driver which can be used to start and test the GUI
 * application. With this implementation no new accounts can be created nor can accounts
 * be removed. The implementation provides one account which supports the deposit and
 * withdraw operations.
 * 
 * @see BankDriver
 */
public class Driver implements IBankDriver {
    private IBank bank = null;

    @Override
    public void connect(String[] args) {
        this.bank = new DummyBank();
        System.out.println("connected...");
    }

    @Override
    public void disconnect() {
        this.bank = null;
        System.out.println("disconnected...");
    }

    @Override
    public bank.IBank getBank() {
        return this.bank;
    }

    /** This Dummy-Bank only contains one account. */
    static class DummyBank implements IBank {
        private Map<String, IAccount> accounts = new HashMap<String, IAccount>();
        {
            DummyAccount acc = new DummyAccount();
            this.accounts.put(acc.getNumber(), acc);
        }

        @Override
        public String createAccount(String owner) {
            return null; // returning null signals that account could not be generated
        }

        @Override
        public boolean closeAccount(String number) {
            return false; // false signals unsuccessful closing
        }

        @Override
        public Set<String> getAccountNumbers() {
            return this.accounts.keySet();
        }

        @Override
        public IAccount getAccount(String number) {
            return this.accounts.get(number);
        }

        @Override
        public void transfer(IAccount a, IAccount b, double amount) {
            // since this bank only supports one account, transfer always transfers amount
            // from one to the same account, thus this empty implementation is correct!
        }
    }

    static class DummyAccount implements IAccount {
        private String owner = "Dagobert Duck";
        private String number = "DD-33-4499";
        private double balance;

        @Override
        public String getNumber() {
            return this.number;
        }

        @Override
        public String getOwner() {
            return this.owner;
        }

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public void deposit(double amount) {
            this.balance += amount;
        }

        @Override
        public void withdraw(double amount) {
            this.balance -= amount;
        }

        @Override
        public double getBalance() {
            return this.balance;
        }
    }

}
