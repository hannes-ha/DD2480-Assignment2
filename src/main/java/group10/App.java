package group10;

import org.eclipse.jetty.server.Server;


public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting CI server...");
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
