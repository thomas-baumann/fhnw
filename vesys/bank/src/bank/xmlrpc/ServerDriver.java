package bank.xmlrpc;

import java.io.IOException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import bank.IServerDriver;
import bank.StartClient;

public class ServerDriver implements IServerDriver {

    
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
        WebServer webServer = new WebServer(port);

        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        try {
            phm.addHandler(bank.xmlrpc.FlatBank.class.getName(), bank.xmlrpc.ServerBank.class);
            xmlRpcServer.setHandlerMapping(phm);

            XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer
                    .getConfig();
            serverConfig.setEnabledForExtensions(true);
            serverConfig.setEnabledForExceptions(true);
            serverConfig.setContentLengthOptional(false);
            

            webServer.start();
            System.out.println("Server started at port: " + port);
        } catch (XmlRpcException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    
}
