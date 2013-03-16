package bank.gui.tests;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import bank.IAccount;
import bank.IBank;

public class EfficiencyTest implements BankTest {

    final static int NUMBER_OF_EFF_TESTS = 1000; // TODO to be defined over
                                                 // construcotr

    @Override
    public String getName() {
        return "Efficiency Test";
    }

    @Override
    public boolean isEnabled(int size) {
        return size > 0;
    }

    @Override
    public void runTests(JFrame context, IBank bank, String currentAccountNumber)
            throws Exception {
        final IAccount acc = bank.getAccount(currentAccountNumber);

        String msg;
        try {
            System.gc();
            long st = System.currentTimeMillis();
            for (int i = 1; i <= EfficiencyTest.NUMBER_OF_EFF_TESTS; i++) {
                acc.deposit(i);
                acc.withdraw(i);
            }
            st = System.currentTimeMillis() - st;
            msg = 2 * EfficiencyTest.NUMBER_OF_EFF_TESTS + " operations in " + st
                    / 1000.0 + " Sek\n" + st / (2.0 * EfficiencyTest.NUMBER_OF_EFF_TESTS)
                    + " msec/op";
        } catch (Exception e) {
            msg = "test did throw an exception\n" + e.getMessage();
        }

        JOptionPane.showMessageDialog(context, msg, "Test Result",
                JOptionPane.INFORMATION_MESSAGE);

    }

}
