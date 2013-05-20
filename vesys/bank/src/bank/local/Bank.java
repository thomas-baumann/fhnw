package bank.local;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bank.IAccount;
import bank.IBank;
import bank.IRemoteAccount;
import bank.IRemoteBank;
import bank.InactiveException;
import bank.OverdrawException;

/**
 * Implementation of the IBank interface with full functionality and the inner class
 * Account with the implementation of the IAccount interface.
 * 
 * @see IBank
 * @see IAccount
 * @author Thomas Baumann
 * @version 1.1
 */
public class Bank implements IRemoteBank {

    private Map<String, Account> accounts = new HashMap<String, Account>();

    @Override
    public Set<String> getAccountNumbers() {
        Set<String> set = new HashSet<>();
        for (Account a : this.accounts.values()) {
            if (a.isActive()) {
                set.add(a.getNumber());
            }
        }
        return set;
    }

    @Override
    public String createAccount(String owner) throws RemoteException {
        Account ac = new Account(owner);
        UnicastRemoteObject.exportObject(ac, 0);
        this.accounts.put(ac.getNumber(), ac);
        return ac.getNumber();
    }

    @Override
    public boolean closeAccount(String number) {
        Account a = this.accounts.get(number);
        if (a != null && a.isActive() && a.getBalance() == 0.0) {
            a.active = false;
            return true;
        }
        return false;
    }

    @Override
    public IRemoteAccount getAccount(String number) {
        return this.accounts.get(number);
    }

    @Override
    public void transfer(IAccount from, IAccount to, double amount) throws IOException,
            InactiveException, OverdrawException {
        from.withdraw(amount);
        try {
            to.deposit(amount);
        } catch (Exception e) {
            from.deposit(amount);
            throw e;
        }
    }

    static class Account implements IRemoteAccount {
        private static int accountNumbers;
        private String number;
        private String owner;
        private double balance;
        private boolean active = true;

        Account(String owner) {
            this.owner = owner;
            this.number = Integer.toString(++Account.accountNumbers);

        }

        @Override
        public double getBalance() {
            return this.balance;
        }

        @Override
        public String getOwner() {
            return this.owner;
        }

        @Override
        public String getNumber() {
            return this.number;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public void deposit(double amount) throws InactiveException {
            if (amount < 0) {
                throw new IllegalArgumentException("Amount can not be less then 0.");
            }
            if (!this.isActive()) {
                throw new InactiveException("Account is inactive.");
            }
            this.balance += amount;
        }

        @Override
        public void withdraw(double amount) throws IllegalArgumentException,
                InactiveException, OverdrawException {
            if (amount < 0) {
                throw new IllegalArgumentException("Amount can not be less then 0.");
            }
            if (!this.isActive()) {
                throw new InactiveException("Account is inactive.");
            }
            if (amount > this.getBalance()) {
                throw new OverdrawException("The account has to less amount.");
            }
            this.balance -= amount;
        }

    }
}
