package group10;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.IOException;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import group10.Util;

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
                handlePostRequest(request, response, target);
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
     * @param target   the target endpoint
     * @throws IOException on problem parsing or handling the request
     */
    private void handlePostRequest(HttpServletRequest request, HttpServletResponse response, final String target)
            throws IOException {

        BufferedReader reader = request.getReader();
        try {
            // Parse the POST request
            JSONObject payload = (JSONObject) new JSONParser().parse(reader);

            // Check if it's a commit/push event
            if (Util.isCommitEvent(payload)) {
                runContinuousIntegration(payload);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "POST request is not a push/commit event.");
            }

            response.setStatus(HttpServletResponse.SC_OK);

            // Maybe replace with a logger
            System.out.println("POST request handled.");
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong while handling your POST request.");
        }
    }

    /**
     * Runs the methods required for CI.
     *
     * @param payload the body of the POST request as JSON
     */
    private void runContinuousIntegration(JSONObject payload) {
        // TODO: git clone -> mvn build -> mvn test -> report results
        System.out.println("Running git clone...");
        System.out.println("Running mvn build...");
        System.out.println("Running mvn test...");
        System.out.println("Result: SUCCESS");
    }
}
