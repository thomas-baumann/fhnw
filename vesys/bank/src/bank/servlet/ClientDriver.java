package bank.servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import bank.InactiveException;
import bank.OverdrawException;
import bank.StartClient;
import bank.communication.AbstractClientDriver;
import bank.communication.answer.IAnswer;
import bank.communication.request.IRequest;

/**
 * This class provides an implementation of the AbstractClientDriver with HTTP.
 * 
 * @see AbstractClientDriver
 * @author Thomas Baumann
 * @version 1.0
 */
public final class ClientDriver extends AbstractClientDriver {

    private URL url;

    @Override
    public void connect(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java " + StartClient.class.getName() + " "
                    + ClientDriver.class.getName() + " <server>");
            System.exit(1);
        }
        this.url = new URL(args[0]);
        this.bank = new SocketBank();
    }

    @Override
    public void disconnect() throws IOException {
        this.bank = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T handleMessage(IRequest request) throws ClassNotFoundException,
            IOException, IllegalArgumentException, OverdrawException, InactiveException,
            ClassCastException {
        HttpURLConnection c = (HttpURLConnection) this.url.openConnection();
        c.setRequestMethod("POST");
        c.setRequestProperty("Content-Type", "application/x-java-serialized-object");
        c.setDoOutput(true);
        c.setDoInput(true);
        c.connect();

        // write object
        ObjectOutputStream oout = new ObjectOutputStream(c.getOutputStream());
        oout.writeObject(request);
        oout.flush();
        oout.close();

        // read object
        ObjectInputStream oin = new ObjectInputStream(c.getInputStream());
        Object o = oin.readObject();
        oin.close();

        c.disconnect();
        return ((IAnswer<T>) o).getData();

    }
}
