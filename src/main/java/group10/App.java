package group10;

import org.eclipse.jetty.server.Server;

/**
 * Contains the main class for the Application to set up and start the server
 */
public class App {
    /**
     * The main function which sets up and starts the server
     *
     * @param args the command-line arguments (if any)
     * @throws Exception is thrown when the set-up or start of the server fails
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Starting CI server...");
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
