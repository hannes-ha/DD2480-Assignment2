package group10;

import org.eclipse.jetty.server.Server;

/**
 * Contains the main function to set up and start the server
 */
public class App {
    /**
     *
     * @param args the command-line arguments (if any)
     * @throws Exception when server set up or start fails
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Starting CI server...");
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
