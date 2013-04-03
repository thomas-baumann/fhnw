package bank.xmlrpc;

import java.io.IOException;

import bank.InactiveException;
import bank.OverdrawException;

public interface FlatBank {

    String createAccount(String owner) throws IOException;

    boolean closeAccount(String number) throws IOException;

    Object[] getAccountNumbers() throws IOException;

    boolean getAccount(String number) throws IOException;

    Object transfer(String fromNumber, String toNumber, double amount) throws IOException,
            IllegalArgumentException, OverdrawException, InactiveException;

    String getOwner(String number) throws IOException;

    boolean isActive(String number) throws IOException;

    Object deposit(String number, double amount) throws IOException, IllegalArgumentException,
            InactiveException;

    Object withdraw(String number, double amount) throws IOException, IllegalArgumentException,
            OverdrawException, InactiveException;

    double getBalance(String number) throws IOException;

}
