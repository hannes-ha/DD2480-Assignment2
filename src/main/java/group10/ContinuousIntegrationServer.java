package group10;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * ContinuousIntegrationServer which acts as webhook for CI tasks.
 * See the Jetty documentation for API documentation of those classes.
 */

public class ContinuousIntegrationServer extends AbstractHandler {

    /**
     * Handles incoming requests by calling appropriate methods for
     * either POST or GET requests.
     *
     * @param target      the target endpoint
     * @param baseRequest the base request
     * @param request     the request (servletrequest)
     * @param response    the response (servletresponse)
     * @throws IOException if unable to parse
     */
    @Override
    public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=utf-8");
        baseRequest.setHandled(true);

        try {
            String requestMethod = request.getMethod();
            if ("GET".equalsIgnoreCase(requestMethod)) {
                handleGetRequest(request, response, target);
            } else if ("POST".equalsIgnoreCase(requestMethod)) {
                handlePostRequest(request, response);
                response.getWriter().println("CI job done");
            }

        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Something went wrong handling your request.");
        }
    }

    /**
     * Handles GET requests.
     *
     * @param request  the request
     * @param response the response
     * @param target   the target endpoints
     * @throws IOException on problem parsing or handling the request
     */
    private void handleGetRequest(HttpServletRequest request, HttpServletResponse response, final String target)
            throws IOException {
        System.out.println(request.getPathInfo());

        try {
            // if subpath is /history, list all files in history folder
            if (request.getPathInfo().equals("/history")) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("<h1>History</h1>");
                File dir = new File("history");
                File[] files = dir.listFiles();
                response.getWriter().println("<ul>");
                for (File file : files) {
                    if (file.isFile()) {
                        response.getWriter()
                                .println(
                                        "<li><a href='history/" + file.getName() + "'>" + file.getName() + "</a></li>");
                    }
                }
                response.getWriter().println("</ul>");
                response.getWriter().println("<a href='/'> << Go back</a>");

                // if its a file in history folder, serve it
            } else if (request.getPathInfo().matches("/history/.*")) {
                String filename = request.getPathInfo().substring(9);
                File file = new File("history/" + filename);
                String withoutExtension = "";

                // if we cant extract filename, return 404
                try {
                    withoutExtension = filename.substring(0, filename.lastIndexOf('.'));
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                if (file.isFile()) {
                    response.getWriter().println("<h1>Build log for " + withoutExtension + "</h1>");
                    response.getWriter().println(Files.readString(Path.of("history/" + filename)));
                    response.getWriter().println("<a href='/history'> << Back to history</a>");
                } else {
                    // if file doesnt exist, return 404
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("<h1> CI job done </h1>");
                response.getWriter().println("<p> You can see the history of builds <a href='/history'>here</a></p>");
            }

        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Something went wrong while handling your GET request.");
        }
    }

    /**
     * Handles POST requests.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException on problem parsing or handling the request
     */
    private void handlePostRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        BufferedReader reader = request.getReader();

        try {
            // Parse the POST request
            String jsonString = Util.convertToString(reader);
            JSONObject payload = (JSONObject) new JSONParser().parse(jsonString);

            // Check if it's a commit/push event
            if (Util.isCommitEvent(payload)) {
                runContinuousIntegration(payload);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "POST request is not a push/commit event.");
            }

            response.setStatus(HttpServletResponse.SC_OK);

            // Maybe replace with a logger
            System.out.println("POST request handled.");
        } catch (ParseException | IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Something went wrong while handling your POST request.");
        }
    }

    /**
     * Runs the methods required for CI.
     *
     * @param payload the body of the POST request as JSON
     */
    private void runContinuousIntegration(JSONObject payload) {
        System.out.println("____________________________________________________________");
        System.out.println("New CI request, running CI for " + Util.getRepositoryName(payload));
        System.out.println("____________________________________________________________");

        // Updated throughout process
        BuildStatus finalStatus = BuildStatus.PENDING;

        // Used to keep track of what happened.
        BuildStatus cloneStatus = BuildStatus.PENDING;
        BuildStatus buildStatus = BuildStatus.PENDING;
        BuildStatus testsStatus = BuildStatus.PENDING;

        String commitHash = Util.getCommitHash(payload);
        CommitStatusHandler statusHandler = new CommitStatusHandler();
        statusHandler.setStatus(commitHash, "pending");

        LogWriter logWriter = new LogWriter();
        logWriter.write("Commit hash: " + commitHash);
        ArrayList<String> mavenOutput = new ArrayList<String>();

        // Running clone
        System.out.println("Running git clone on " + Util.getCloneURL(payload) + " branch " + Util.getBranch(payload));
        logWriter.write("Running git clone on " + Util.getCloneURL(payload) + " branch " + Util.getBranch(payload));

        boolean cloneSuccess = true;
        try {
            GitRunner.cloneRepo(Util.getCloneURL(payload), Util.getBranch(payload), "build");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            logWriter.write(e.getMessage());
            cloneSuccess = false;
        }

        if (!cloneSuccess) {

            finalStatus = BuildStatus.CLONE_FAILED;
            cloneStatus = BuildStatus.CLONE_FAILED;
        }
        // If clone succeeded, run maven compile
        else {
            finalStatus = BuildStatus.CLONE_SUCCEEDED;
            cloneStatus = BuildStatus.CLONE_SUCCEEDED;

            // Running mvn
            System.out.println("Running mvn compile...");
            logWriter.write("Running mvn compile...");

            MavenRunner mavenRunner = new MavenRunner("./build/.");
            finalStatus = mavenRunner.runMvnCompile();

            buildStatus = finalStatus;

            // If compile succeeded, run tests
            if (finalStatus != BuildStatus.BUILD_FAILED) {
                System.out.println("Running mvn test...");
                logWriter.write("Running mvn test...");
                testsStatus = mavenRunner.runMvnTest();
                finalStatus = testsStatus;
            }
            mavenOutput = mavenRunner.getBuildLogs();
        }

        System.out.println("____________________________________________________________");
        System.out.println("Results:");
        System.out.println(cloneStatus);
        System.out.println(buildStatus);
        System.out.println(testsStatus);
        System.out.println("____________________________________________________________");

        logWriter.write("Results:");
        logWriter.write(cloneStatus.toString());
        logWriter.write(buildStatus.toString());
        logWriter.write(testsStatus.toString());

        logWriter.write("Maven output (compile & test):");

        StringBuilder sb = new StringBuilder();
        for (String line : mavenOutput) {
            logWriter.write(line);

            if (!(line.contains("Downloaded") || line.contains("Downloading"))) {
                sb.append(line);
            }
        }
        logWriter.close();

        if (finalStatus == BuildStatus.TESTS_SUCCEEDED) {
            statusHandler.setStatus(commitHash, "success");
        } else {
            statusHandler.setStatus(commitHash, "failure");
        }

        String emailMsg = "CI results for " + Util.getRepositoryName(payload) + ":\n\n" 
                + cloneStatus + "\n" 
                + buildStatus + "\n" 
                + testsStatus + "\n"
                + sb.toString() + "\n\n";

        MailHandler mailHandler = new MailHandler();
        mailHandler.emailResults("hanhal@kth.se, estolpe@kth.se, abaz@kth.se, tmatts@kth.se", emailMsg);

    }

    /**
     *  Represents a group of BuildStatus constants
     */
    public enum BuildStatus {
        /**
         * Build status is pending
         */
        PENDING,
        /**
         * Build status is that the cloning succeeded
         */
        CLONE_SUCCEEDED,
        /**
         * Build status is that the cloning failed
         */
        CLONE_FAILED,
        /**
         * Build status is that the build succeeded
         */
        BUILD_SUCCEEDED,
        /**
         * Build status is that the build failed
         */
        BUILD_FAILED,
        /**
         * Build status is that the tests succeeded
         */
        TESTS_SUCCEEDED,
        /**
         * Build status is that the tests failed
         */
        TESTS_FAILED
    }

}
