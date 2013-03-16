package bank.communication;

import java.io.IOException;
import java.util.Set;

import bank.IAccount;
import bank.IBank;
import bank.IBankDriver;
import bank.InactiveException;
import bank.OverdrawException;
import bank.communication.request.CloseAccountRequest;
import bank.communication.request.CreateAccountRequest;
import bank.communication.request.DepositRequest;
import bank.communication.request.GetAccountNumbersRequest;
import bank.communication.request.GetAccountRequest;
import bank.communication.request.GetBalanceRequest;
import bank.communication.request.GetOwnerRequest;
import bank.communication.request.IRequest;
import bank.communication.request.IsActiveRequest;
import bank.communication.request.TransferRequest;
import bank.communication.request.WithdrawRequest;

/**
 * This abstract class provides an implementation of the IBankDriver interface with the
 * additional method handleMessage(...) to send and receive objects.
 * 
 * @see IBankDriver
 * @author Thomas Baumann
 * @version 1.1
 */
public abstract class AbstractClientDriver implements IBankDriver {

    protected IBank bank;

    @Override
    public final IBank getBank() {
        return this.bank;
    }

    /**
     * Sends a request object and receives afterwards the answer object and returns the
     * data from the answer object.
     * 
     * @param r the request to write
     * @return answer object
     * @throws ClassNotFoundException Class of a read object cannot be found
     * @throws IOException When an IO problem occurs
     * @throws IllegalArgumentException When answer is an IllegalArgumentException
     * @throws OverdrawException When answer is an OverdrawException
     * @throws InactiveException When answer is an InactiveException
     */
    protected abstract <T> T handleMessage(IRequest r) throws ClassNotFoundException,
            IOException, IllegalArgumentException, OverdrawException, InactiveException,
            ClassCastException;

    protected final class SocketBank implements IBank {

        // public constructor for visibility
        public SocketBank() {}

        @Override
        public Set<String> getAccountNumbers() throws IOException {
            try {
                return AbstractClientDriver.this
                        .handleMessage(new GetAccountNumbersRequest());
            } catch (ClassNotFoundException | IllegalArgumentException
                    | OverdrawException | InactiveException | ClassCastException e) {
                throw new IOException(e);
            }
        }

        @Override
        public String createAccount(String owner) throws IOException {
            try {
                return AbstractClientDriver.this.handleMessage(new CreateAccountRequest(
                        owner));
            } catch (ClassNotFoundException | IllegalArgumentException
                    | OverdrawException | InactiveException | ClassCastException e) {
                throw new IOException(e);
            }
        }

        @Override
        public boolean closeAccount(String number) throws IOException {
            try {
                return AbstractClientDriver.this.handleMessage(new CloseAccountRequest(
                        number));
            } catch (ClassNotFoundException | IllegalArgumentException
                    | OverdrawException | InactiveException | ClassCastException e) {
                throw new IOException(e);
            }
        }

        @Override
        public IAccount getAccount(String number) throws IOException {
            try {
                if (AbstractClientDriver.this
                        .handleMessage(new GetAccountRequest(number))) {
                    return new SocketAccount(number);
                } else {
                    return null;
                }
            } catch (ClassNotFoundException | IllegalArgumentException
                    | OverdrawException | InactiveException | ClassCastException e) {
                throw new IOException(e);
            }
        }

        @Override
        public void transfer(IAccount from, IAccount to, double amount)
                throws IOException, IllegalArgumentException, OverdrawException,
                InactiveException {
            try {
                AbstractClientDriver.this.handleMessage(new TransferRequest(from
                        .getNumber(), to.getNumber(), amount));
            } catch (ClassNotFoundException | ClassCastException e) {
                throw new IOException(e);
            }
        }

    }

    protected final class SocketAccount implements IAccount {
        private String number;

        public SocketAccount(String number) {
            this.number = number;
        }

        @Override
        public double getBalance() throws IOException {
            try {
                return AbstractClientDriver.this.handleMessage(new GetBalanceRequest(
                        this.number));
            } catch (ClassNotFoundException | IllegalArgumentException
                    | OverdrawException | InactiveException | ClassCastException e) {
                throw new IOException(e);
            }
        }

        @Override
        public String getOwner() throws IOException {
            try {
                return AbstractClientDriver.this.handleMessage(new GetOwnerRequest(
                        this.number));
            } catch (ClassNotFoundException | IllegalArgumentException
                    | OverdrawException | InactiveException | ClassCastException e) {
                throw new IOException(e);
            }
        }

        @Override
        public String getNumber() {
            return this.number;
        }

        @Override
        public boolean isActive() throws IOException {
            try {
                return AbstractClientDriver.this.handleMessage(new IsActiveRequest(
                        this.number));
            } catch (ClassNotFoundException | IllegalArgumentException
                    | OverdrawException | InactiveException | ClassCastException e) {
                throw new IOException(e);
            }
        }

        @Override
        public void deposit(double amount) throws IllegalArgumentException,
                InactiveException, IOException {
            try {
                AbstractClientDriver.this.handleMessage(new DepositRequest(this.number,
                        amount));
            } catch (ClassNotFoundException | OverdrawException | ClassCastException e) {
                throw new IOException(e);
            }
        }

        @Override
        public void withdraw(double amount) throws IllegalArgumentException,
                InactiveException, OverdrawException, IOException {
            try {
                AbstractClientDriver.this.handleMessage(new WithdrawRequest(this.number,
                        amount));
            } catch (ClassNotFoundException | ClassCastException e) {
                throw new IOException(e);
            }
        }
    }

}
