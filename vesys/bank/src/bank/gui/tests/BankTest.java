package bank.gui.tests;

import javax.swing.JFrame;

import bank.IBank;

public interface BankTest {
    String getName();

    boolean isEnabled(int size);

    void runTests(JFrame context, IBank bank, String currentAccountNumber)
            throws Exception;
}
