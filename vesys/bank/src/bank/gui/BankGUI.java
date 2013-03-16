/*
 * Copyright (c) 2000-2013 Fachhochschule Nordwestschweiz (FHNW) All Rights Reserved.
 */

package bank.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bank.IAccount;
import bank.IBank;
import bank.IBankDriver;
import bank.InactiveException;
import bank.OverdrawException;
import bank.gui.tests.BankTest;

public class BankGUI extends JFrame {

    private IBankDriver driver;
    private IBank bank;

    private JComboBox<String> accountcombo = new JComboBox<>();
    private Map<String, IAccount> accounts = new HashMap<String, IAccount>();

    private JTextField fld_owner = new JTextField();
    private JTextField fld_balance = new JTextField();

    private JButton btn_refresh = new JButton("Refresh");
    private JButton btn_deposit = new JButton("Deposit Money");
    private JButton btn_withdraw = new JButton("Withdraw Money");
    private JButton btn_transfer = new JButton("Transfer Money");

    private JMenuItem item_new = new JMenuItem("New Account...");
    private JMenuItem item_close = new JMenuItem("Close Account");
    private JMenuItem item_exit = new JMenuItem("Exit");
    private JMenuItem item_about = new JMenuItem("About");

    private List<BankTest> tests = new LinkedList<>();
    private Map<BankTest, JMenuItem> testMenuItems = new HashMap<>();

    private boolean ignoreItemChanges = false;

    private BankTest loadTest(String name) {
        try {
            return (BankTest) Class.forName(name).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException
                | ClassCastException e) {
            return null;
        }
    }

    public BankGUI(IBankDriver server) {
        this.driver = server;
        this.bank = server.getBank();

        this.setTitle("ClientBank Application");
        this.setBackground(Color.lightGray);

        BankTest test;
        test = this.loadTest("bank.gui.tests.EfficiencyTest");
        if (test != null) {
            this.tests.add(test);
        }
        test = this.loadTest("bank.gui.tests.ThreadingTest");
        if (test != null) {
            this.tests.add(test);
        }
        test = this.loadTest("bank.gui.tests.FunctionalityTest");
        if (test != null) {
            this.tests.add(test);
        }
        test = this.loadTest("bank.gui.tests.TransferTest");
        if (test != null) {
            this.tests.add(test);
        }

        // define menus
        JMenuBar menubar = new JMenuBar();
        this.setJMenuBar(menubar);

        JMenu menu_file = new JMenu("File");
        menubar.add(menu_file);
        menu_file.add(this.item_new);
        menu_file.add(this.item_close);
        menu_file.addSeparator();
        menu_file.add(this.item_exit);

        JMenu menu_test = new JMenu("Test");
        menubar.add(menu_test);

        for (BankTest t : this.tests) {
            final BankTest tt = t;
            JMenuItem m = new JMenuItem(t.getName());
            this.testMenuItems.put(t, m);
            menu_test.add(m);
            m.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        tt.runTests(BankGUI.this, BankGUI.this.bank,
                                BankGUI.this.currentAccountNumber());
                        BankGUI.this.refreshDialog();
                    } catch (Exception ex) {
                        BankGUI.this.error(ex);
                    }
                }
            });
        }

        JMenu menu_help = new JMenu("Help");
        menubar.add(menu_help);
        menu_help.add(this.item_about);

        this.item_new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BankGUI.this.addAccount();
            }
        });
        this.item_close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BankGUI.this.closeAccount();
            }
        });
        this.item_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BankGUI.this.exit();
            }
        });
        this.item_about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BankGUI.this.about();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                BankGUI.this.exit();
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                BankGUI.this.refreshDialog();
            }
        });

        this.accountcombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (BankGUI.this.ignoreItemChanges) {
                    return;
                }
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    BankGUI.this.updateCustomerInfo();
                }
            }
        });

        // create layout

        this.setResizable(false);

        JPanel center = new JPanel(new GridLayout(3, 2, 5, 5));
        center.add(new JLabel("Account Nr: ", SwingConstants.RIGHT));
        center.add(this.accountcombo);
        center.add(new JLabel("Owner: ", SwingConstants.RIGHT));
        center.add(this.fld_owner);
        center.add(new JLabel("Balance: ", SwingConstants.RIGHT));
        center.add(this.fld_balance);

        // set text fields read only
        this.fld_owner.setEditable(false);
        this.fld_balance.setEditable(false);

        JPanel east = new JPanel(new GridLayout(3, 1, 5, 5));
        east.add(this.btn_deposit);
        east.add(this.btn_withdraw);
        east.add(this.btn_transfer);

        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.add(new JLabel(""), BorderLayout.NORTH);
        p.add(center, BorderLayout.CENTER);
        p.add(east, BorderLayout.EAST);
        p.add(this.btn_refresh, BorderLayout.SOUTH);

        // getContentPane().add(p);
        this.add(p);

        // Add ActionListeners
        this.btn_refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BankGUI.this.refreshDialog();
            }
        });
        this.btn_deposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BankGUI.this.deposit();
            }
        });
        this.btn_withdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BankGUI.this.withdraw();
            }
        });
        this.btn_transfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BankGUI.this.transfer();
            }
        });

        Dimension d = this.accountcombo.getPreferredSize();
        d.setSize(Math.max(d.getWidth(), 130), d.getHeight());
        this.accountcombo.setPreferredSize(d);

        this.refreshDialog();
    }

    public String currentAccountNumber() {
        return (String) this.accountcombo.getSelectedItem();
    }

    public void addAccount() {
        AddAccountDialog addaccount = new AddAccountDialog(this, "Add Account");

        Point loc = this.getLocation();
        addaccount.setLocation(loc.x + 50, loc.y + 50);
        addaccount.setModal(true);
        addaccount.setVisible(true);

        if (!addaccount.canceled()) {
            String number = null;
            try {
                number = this.bank.createAccount(addaccount.getOwnerName());
            } catch (Exception e) {
                this.error(e);
            }

            if (number == null) {
                JOptionPane.showMessageDialog(this, "Account could not be created",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    IAccount acc = this.bank.getAccount(number);
                    this.accounts.put(number, acc);

                    String str = addaccount.getBalance().trim();
                    double amount;
                    if (str.equals("")) {
                        amount = 0;
                    } else {
                        amount = Double.parseDouble(str);
                    }
                    acc.deposit(amount);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Illegal Format", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, "Illegal Argument", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (InactiveException e) {
                    JOptionPane.showMessageDialog(this, "Account is inactive", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    this.error(e);
                }
                this.ignoreItemChanges = true;
                this.accountcombo.addItem(number);
                this.accountcombo.setSelectedItem(number);
                this.ignoreItemChanges = false;
                this.refreshDialog();
            }
        }
    }

    public void closeAccount() {
        String number = this.currentAccountNumber();
        if (number != null) {
            int res = JOptionPane.showConfirmDialog(this, "Really close account "
                    + number + "?", "Confirm closing", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (res == 0) {
                try {
                    boolean done = this.bank.closeAccount(number);
                    if (done) {
                        this.refreshDialog();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Account could not be closed", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    this.error(e);
                }
            }
        }
    }

    public void deposit() {
        String number = this.currentAccountNumber();
        if (number != null) {
            String s = JOptionPane.showInputDialog(this, "Enter amount to deposit:",
                    "Deposit Money", JOptionPane.QUESTION_MESSAGE);
            if (s != null) {
                try {
                    double amount = Double.parseDouble(s);
                    IAccount a = this.accounts.get(number);
                    a.deposit(amount);
                    this.fld_balance.setText(this.currencyFormat(a.getBalance()));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Illegal Value", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, "Illegal Argument", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (InactiveException e) {
                    JOptionPane.showMessageDialog(this, "Account is inactive", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    this.error(e);
                }
            }
        }
    }

    public void withdraw() {
        String number = this.currentAccountNumber();
        if (number != null) {
            String s = JOptionPane.showInputDialog(this, "Enter amount to draw:",
                    "Draw Money", JOptionPane.QUESTION_MESSAGE);
            if (s != null) {
                try {
                    double amount = Double.parseDouble(s);
                    IAccount a = this.accounts.get(number);
                    a.withdraw(amount);
                    this.fld_balance.setText(this.currencyFormat(a.getBalance()));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Illegal Value", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, "Illegal Argument", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (InactiveException e) {
                    JOptionPane.showMessageDialog(this, "Account is inactive", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (OverdrawException e) {
                    JOptionPane.showMessageDialog(this, "Account must not be overdrawn",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    this.error(e);
                }
            }
        }
    }

    public void transfer() {
        String number = this.currentAccountNumber();
        if (number != null) {
            try {
                Set<String> s = new HashSet<String>(this.accounts.keySet());
                s.remove(number);

                TransferDialog trans = new TransferDialog(this, "Transfer Money", number,
                        s);
                Point loc = this.getLocation();
                trans.setLocation(loc.x + 50, loc.y + 50);
                trans.setModal(true);
                trans.setVisible(true);

                if (!trans.canceled()) {
                    if (number.equals(trans.getAccountNumber())) {
                        JOptionPane.showMessageDialog(this,
                                "Both Accounts are the same!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            double amount = Double.parseDouble(trans.getBalance());
                            IAccount from = this.accounts.get(number);
                            IAccount to = this.accounts.get(trans.getAccountNumber());
                            this.bank.transfer(from, to, amount);

                            // after transfer adjust value of displayed account
                            this.fld_balance.setText(this.currencyFormat(from
                                    .getBalance()));

                            JOptionPane.showMessageDialog(this, "Transfer successfull",
                                    "Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(this, "Illegal Balance",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (IllegalArgumentException e) {
                            JOptionPane.showMessageDialog(this, "Illegal Argument",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (InactiveException e) {
                            JOptionPane.showMessageDialog(this,
                                    "At least one account is inactive", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } catch (OverdrawException e) {
                            JOptionPane.showMessageDialog(this,
                                    "Account must not be overdrawn", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (Exception e) {
                this.error(e);
            }
        }
    }

    public void exit() {
        try {
            this.driver.disconnect();
        } catch (IOException e) {
            // TODO what to do with IOException upon disconnection
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void refreshDialog() {
        String nr = this.currentAccountNumber();
        this.accountcombo.removeAllItems();
        if (this.bank != null) {
            try {
                Set<String> s = this.bank.getAccountNumbers();
                ArrayList<String> accnumbers = new ArrayList<String>(s);
                Collections.sort(accnumbers);
                this.ignoreItemChanges = true;
                for (String item : accnumbers) {
                    this.accountcombo.addItem(item);
                    if (item.equals(nr)) {
                        this.accountcombo.setSelectedItem(item);
                    }
                }
                this.ignoreItemChanges = false;

                // clean up local accounts map
                for (String key : s) {
                    if (!this.accounts.containsKey(key)) {
                        this.accounts.put(key, this.bank.getAccount(key));
                    }
                }
                Iterator<String> it = this.accounts.keySet().iterator();
                while (it.hasNext()) {
                    if (!s.contains(it.next())) {
                        it.remove();
                    }
                }

                int size = s.size();
                this.btn_deposit.setEnabled(size > 0);
                this.btn_withdraw.setEnabled(size > 0);
                this.btn_transfer.setEnabled(size > 1);
                this.item_close.setEnabled(size > 0);

                for (BankTest t : this.tests) {
                    JMenuItem m = this.testMenuItems.get(t);
                    m.setEnabled(t.isEnabled(size));
                }

                this.updateCustomerInfo();
            } catch (Exception e) {
                this.error(e);
            }
        }
    }

    private void updateCustomerInfo() {
        String nr = this.currentAccountNumber();
        try {
            if (nr != null) {
                IAccount a = this.accounts.get(nr);
                if (a != null) {
                    this.fld_owner.setText(a.getOwner());
                    this.fld_balance.setText(this.currencyFormat(a.getBalance()));
                } else {
                    JOptionPane.showMessageDialog(this, "Account not found", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    this.refreshDialog();
                }
            } else {
                this.fld_owner.setText("");
                this.fld_balance.setText("");
            }
        } catch (Exception e) {
            this.error(e);
        }
    }

    public String currencyFormat(double val) {
        return NumberFormat.getCurrencyInstance().format(val);
    }

    public void error(Exception e) {
        JDialog dlg = new ErrorBox(this, e);
        dlg.setModal(true);
        dlg.setVisible(true);
    }

    public void about() {
        AboutBox dlg = new AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = this.getSize();
        Point loc = this.getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.setVisible(true);
    }

    static class ErrorBox extends JDialog {
        public ErrorBox(Frame parent, Exception e) {
            super(parent);
            this.setTitle("Exception");
            this.setResizable(true);

            // JTextField msg1 = new JTextField();
            // msg1.setText(e.getMessage());

            JTextArea trace = new JTextArea(10, 50);
            java.io.StringWriter buf = new java.io.StringWriter();
            java.io.PrintWriter wr = new java.io.PrintWriter(buf);
            e.printStackTrace(wr);
            trace.setText(buf.toString());
            trace.setCaretPosition(0);
            trace.setEditable(false);

            JScrollPane msg = new JScrollPane(trace);

            JButton ok = new JButton("OK");

            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ErrorBox.this.dispose();
                }
            });

            // getContentPane().add(msg1, BorderLayout.NORTH);
            this.getContentPane().add(msg, BorderLayout.CENTER);
            this.getContentPane().add(ok, BorderLayout.SOUTH);
            this.getRootPane().setDefaultButton(ok);
            ok.requestFocus();
            this.pack();
        }
    }

    static class AboutBox extends JDialog {

        public AboutBox(Frame parent) {
            super(parent);
            this.setTitle("About Bank Client");
            this.setResizable(false);

            JPanel p_text = new JPanel(new GridLayout(0, 1));
            p_text.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
            p_text.add(new JLabel("Distributed Systems", SwingConstants.CENTER));
            p_text.add(new JLabel("Bank Client", SwingConstants.CENTER));
            p_text.add(new JLabel("", SwingConstants.CENTER));
            p_text.add(new JLabel("� D. Gruntz, 2001-2013", SwingConstants.CENTER));

            JButton ok = new JButton("OK");
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AboutBox.this.dispose();
                }
            });

            this.getContentPane().add(p_text, BorderLayout.CENTER);
            this.getContentPane().add(ok, BorderLayout.SOUTH);
            this.pack();
        }
    }

    static class AddAccountDialog extends JDialog {
        private JTextField ownerfield = new JTextField(12);
        private JTextField balancefield = new JTextField(12);

        private boolean canceled = true;

        AddAccountDialog(Frame owner, String title) {

            super(owner, title);

            // Create Layout
            JButton btn_ok = new JButton("Ok");
            JButton btn_cancel = new JButton("Cancel");
            JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
            p.add(new JLabel("Owner:", SwingConstants.RIGHT));
            p.add(this.ownerfield);
            p.add(new JLabel("Balance:", SwingConstants.RIGHT));
            p.add(this.balancefield);
            p.add(btn_ok);
            p.add(btn_cancel);

            this.getContentPane().add(p);

            btn_ok.setDefaultCapable(true);
            this.getRootPane().setDefaultButton(btn_ok);

            // Add ActionListeners
            btn_ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddAccountDialog.this.canceled = false;
                    AddAccountDialog.this.setVisible(false);
                }
            });
            btn_cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddAccountDialog.this.canceled = true;
                    AddAccountDialog.this.setVisible(false);
                }
            });
            this.pack();
        }

        public boolean canceled() {
            return this.canceled;
        }

        public String getOwnerName() {
            return this.ownerfield.getText();
        }

        public String getBalance() {
            return this.balancefield.getText();
        }
    }

    static class TransferDialog extends JDialog {
        private JTextField balancefield = new JTextField(12);
        private JComboBox<String> accountcombo;

        private boolean canceled = true;

        TransferDialog(Frame owner, String title, String account, Set<String> accounts) {
            super(owner, title);

            JButton btn_ok = new JButton("Ok");
            JButton btn_cancel = new JButton("Cancel");
            ArrayList<String> accnumbers = new ArrayList<String>(accounts);
            Collections.sort(accnumbers);
            this.accountcombo = new JComboBox<>(accnumbers.toArray(new String[] {}));

            // Create Layout
            JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));
            p.add(new JLabel("From Account:", SwingConstants.RIGHT));
            p.add(new JLabel(account));
            p.add(new JLabel("To Account:", SwingConstants.RIGHT));
            p.add(this.accountcombo);
            p.add(new JLabel("Amount:", SwingConstants.RIGHT));
            p.add(this.balancefield);
            p.add(btn_ok);
            p.add(btn_cancel);

            this.getContentPane().add(p);
            btn_ok.setDefaultCapable(true);
            this.getRootPane().setDefaultButton(btn_ok);

            // Add ActionListeners
            btn_ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TransferDialog.this.canceled = false;
                    TransferDialog.this.setVisible(false);
                }
            });
            btn_cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TransferDialog.this.canceled = true;
                    TransferDialog.this.setVisible(false);
                }
            });
            this.pack();
        }

        public boolean canceled() {
            return this.canceled;
        }

        public String getAccountNumber() {
            return (String) this.accountcombo.getSelectedItem();
        }

        public String getBalance() {
            return this.balancefield.getText();
        }
    }

}
