package group10;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

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
            }
            response.getWriter().println("CI job done");

        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong handling your request.");
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

        try {
            // TODO: GET request logic
            // Check the target endpoint, provide log or something, if we're going for P+
            // Otherwise I don't think we even need to handle GET requests at all

            response.setStatus(HttpServletResponse.SC_OK);

            // Maybe we should have a logger? The server keeps complaining about SLF4J
            response.getWriter().println("GET request handled.");
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong while handling your GET request.");
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
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong while handling your POST request.");
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

        // Running clone
        System.out.println("Running git clone on " + Util.getCloneURL(payload) + " branch " + Util.getBranch(payload));
        boolean cloneSuccess = true;
        try {
            GitRunner.cloneRepo(Util.getCloneURL(payload), Util.getBranch(payload));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
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

            MavenRunner mavenRunner = new MavenRunner("./build");
            finalStatus = mavenRunner.runMvnCompile();

            buildStatus = finalStatus;

            // If compile succeeded, run tests
            if (finalStatus != BuildStatus.BUILD_FAILED) {
                System.out.println("Running mvn test...");
                testsStatus = mavenRunner.runMvnTest();
            }
        }

        System.out.println("____________________________________________________________");
        System.out.println("Results:");
        System.out.println(cloneStatus);
        System.out.println(buildStatus);
        System.out.println(testsStatus);
        System.out.println("____________________________________________________________");
    }

    public enum BuildStatus {
        PENDING,
        CLONE_SUCCEEDED,
        CLONE_FAILED,
        BUILD_SUCCEEDED,
        BUILD_FAILED,
        TESTS_SUCCEEDED,
        TESTS_FAILED
    }

}
