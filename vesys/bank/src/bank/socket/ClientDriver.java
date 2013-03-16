package bank.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import bank.InactiveException;
import bank.OverdrawException;
import bank.StartClient;
import bank.communication.AbstractClientDriver;
import bank.communication.answer.IAnswer;
import bank.communication.request.IRequest;

/**
 * This class provides an implementation of the AbstractClientDriver with sockets.
 * 
 * @see AbstractClientDriver
 * @author Thomas Baumann
 * @version 1.1
 */
public final class ClientDriver extends AbstractClientDriver {

    private Socket socket;
    private ObjectOutputStream oout;
    private ObjectInputStream oin;

    @Override
    public void connect(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java " + StartClient.class.getName() + " "
                    + ClientDriver.class.getName() + " <port>");
            System.exit(1);
        }
        int port = 0;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Port must be a number");
            System.exit(1);
        }
        this.socket = new Socket(args[0], port);
        this.socket.setTcpNoDelay(true);
        this.oout = new ObjectOutputStream(this.socket.getOutputStream());
        this.oin = new ObjectInputStream(this.socket.getInputStream());
        this.bank = new SocketBank();
        System.out.println("connected...");
    }

    @Override
    public void disconnect() throws IOException {
        try {
            // send null to close connection
            this.handleMessage(null);
        } catch (ClassNotFoundException | IllegalArgumentException | ClassCastException
                | OverdrawException | InactiveException e) {
            throw new IOException(e);
        }
        this.bank = null;
        this.socket.close();
        System.out.println("disconnected...");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final <T> T handleMessage(IRequest r) throws ClassNotFoundException,
            IOException, IllegalArgumentException, OverdrawException, InactiveException,
            ClassCastException {
        this.oout.writeObject(r);
        this.oout.flush();
        Object o = this.oin.readObject();
        return ((IAnswer<T>) o).getData();
    }

}
