package bank.xmlrpc;

import java.io.IOException;

import bank.IAccount;
import bank.IBank;
import bank.InactiveException;
import bank.OverdrawException;

public class ServerBank implements FlatBank {

    private static final IBank bank = new bank.local.Bank();

    @Override
    public String createAccount(String owner) throws IOException {
        return ServerBank.bank.createAccount(owner);
    }

    @Override
    public boolean closeAccount(String number) throws IOException {
        return ServerBank.bank.closeAccount(number);
    }

    @Override
    public Object[] getAccountNumbers() throws IOException {
        return ServerBank.bank.getAccountNumbers().toArray();
    }

    @Override
    public boolean getAccount(String number) throws IOException {
        return ServerBank.bank.getAccount(number) != null;
    }

    @Override
    public Object transfer(String fromNumber, String toNumber, double amount)
            throws IOException, IllegalArgumentException, OverdrawException,
            InactiveException {
        IAccount from = ServerBank.bank.getAccount(fromNumber);
        IAccount to = ServerBank.bank.getAccount(toNumber);
        ServerBank.bank.transfer(from, to, amount);
        return null;
    }

    @Override
    public String getOwner(String number) throws IOException {
        IAccount acc = ServerBank.bank.getAccount(number);
        if (acc == null) {
            throw new IOException();
        } else {
            return acc.getOwner();
        }
    }

    @Override
    public boolean isActive(String number) throws IOException {
        IAccount acc = ServerBank.bank.getAccount(number);
        if (acc == null) {
            throw new IOException();
        } else {
            return acc.isActive();
        }
    }

    @Override
    public Object deposit(String number, double amount) throws IOException,
            IllegalArgumentException, InactiveException {
        IAccount acc = ServerBank.bank.getAccount(number);
        if (acc == null) {
            throw new IOException();
        } else {
            acc.deposit(amount);
        }
        return null;
    }

    @Override
    public Object withdraw(String number, double amount) throws IOException,
            IllegalArgumentException, OverdrawException, InactiveException {
        IAccount acc = ServerBank.bank.getAccount(number);
        if (acc == null) {
            throw new IOException();
        } else {
            acc.withdraw(amount);
        }
        return null;
    }

    @Override
    public double getBalance(String number) throws IOException {
        IAccount acc = ServerBank.bank.getAccount(number);
        if (acc == null) {
            throw new IOException();
        } else {
            return acc.getBalance();
        }
    }
}