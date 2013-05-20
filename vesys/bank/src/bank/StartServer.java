package bank;

import java.io.IOException;

/**
 * Class StartServer is used to start the Server side of the bank application. As a
 * runtime parameter the name of the class which implements the <code>IServerDriver</code>
 * interface has to be specified. This class is then loaded and used as the server. This
 * class needs a public constructor.
 * 
 * <pre>
 * Usage: java bank.StartServer &lt;classname&gt;
 * </pre>
 * 
 * E.g. start the application with one of the following commands. The additional runtime
 * arguments are passed to the start method of the IServerDriver implementation.
 * 
 * <pre>
 * java bank.StartServer bank.socket.ServerDriver
 * </pre>
 * 
 * @author Thomas Baumann
 * @version 1.0
 */
public final class StartServer {

    /** Utility class which is only used to start the application */
    private StartServer() {}

    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("Usage: java " + StartServer.class.getName() + " <class>");
            System.exit(1);
        }

        IServerDriver server = null;
        try {
            Class<?> c = Class.forName(args[0]);
            server = (IServerDriver) c.newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("class " + args[0] + " coult not be found");
            System.exit(1);
        } catch (InstantiationException e) {
            System.out.println("class " + args[0] + " could not be instantiated");
            System.out.println("probably it has no public default constructor!");
            System.exit(1);
        } catch (IllegalAccessException e) {
            System.out.println("class " + args[0] + " could not be instantiated");
            System.out.println("probably it is not declared public!");
            System.exit(1);
        }

        String[] serverArgs = new String[args.length - 1];
        System.arraycopy(args, 1, serverArgs, 0, args.length - 1);

        try {
            server.start(serverArgs);
            System.out.println("Bank server started...");
        } catch (IOException e) {
            System.out.println("Problem while starting the server:");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
