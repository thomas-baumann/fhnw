package bank.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import bank.IBank;
import bank.IServerDriver;
import bank.StartClient;
import bank.communication.answer.IAnswer;
import bank.communication.request.IRequest;
import bank.local.Bank;

/**
 * This class provides an implementation of the IServerDriver interface which creates a
 * ServerSocket.
 * 
 * @see IServerDriver
 * @author Thomas Baumann
 * @version 1.1
 */
public class ServerDriver implements IServerDriver {

    private IBank bank = new Bank();

    @Override
    public void start(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java " + StartClient.class.getName() + " "
                    + ServerDriver.class.getName() + " <portnumber>");
            System.exit(1);
        }
        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Port must be a number");
            System.exit(1);
        }

        ServerSocket server = new ServerSocket(port);
        try {
            while (true) {
                try {
                    Socket s = server.accept();
                    s.setTcpNoDelay(true);
                    Thread t = new Thread(new BankServerHandler(s));
                    t.start();
                } catch (IOException e) {
                    System.out.println("Problem while connection to a client");
                    e.printStackTrace();
                }

            }
        } finally {
            server.close();
        }
    }

    private class BankServerHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream oin;
        private ObjectOutputStream oout;

        public BankServerHandler(Socket s) throws IOException {
            this.socket = s;
            this.oout = new ObjectOutputStream(this.socket.getOutputStream());
            this.oin = new ObjectInputStream(this.socket.getInputStream());
        }

        @Override
        public void run() {
            try {
                boolean connected = true;
                while (connected) {
                    try {
                        Object o = this.oin.readObject();
                        if (o != null) {
                            IAnswer<?> answer = ((IRequest) o)
                                    .handleRequest(ServerDriver.this.bank);
                            this.oout.writeObject(answer);
                            this.oout.flush();
                        } else {
                            connected = false;
                        }
                    } catch (ClassNotFoundException e) {
                        System.out.println("invalid object arrived");
                        e.printStackTrace();
                    }
                }
                this.socket.close();
            } catch (IOException e) {
                System.out.println("problem with the connection occured");
                e.printStackTrace();
            }
        }

    }

}
