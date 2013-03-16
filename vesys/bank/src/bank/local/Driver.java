package bank.local;

import bank.IBank;
import bank.IBankDriver;

/**
 * This class provides an local implementation of the IBankDriver which means the bank is
 * coupled to the GUI
 * 
 * @see IBankDriver
 * @author Thomas Baumann
 * @version 1.0
 */
public class Driver implements IBankDriver {
    private IBank bank;

    @Override
    public void connect(String[] args) {
        this.bank = new Bank();
        System.out.println("connected...");
    }

    @Override
    public void disconnect() {
        this.bank = null;
        System.out.println("disconnected...");
    }

    @Override
    public IBank getBank() {
        return this.bank;
    }

}