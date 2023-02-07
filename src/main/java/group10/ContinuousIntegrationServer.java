package group10;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
 
import java.io.IOException;
 
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/** 
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/

public class ContinuousIntegrationServer extends AbstractHandler {
    
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
                       throws IOException, ServletException {
    
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code

        // Create string array with 'mvn' and 'build' as arguments
        boolean builds = false;
        int ret = 0;
        try {
            String[] commands = {"mvn", "build"};
            Process p = Runtime.getRuntime().exec(commands);
            ret = p.waitFor();
            builds = ret == 0;
        } catch (Exception e) {
            builds = false;
        }
        String buildStatus = builds ? "Success" : "Failed";

        response.getWriter().println("<h1>CI job done</h1>");
        response.getWriter().println("<ul>");
        response.getWriter().println("<li>Build status: " + buildStatus + ret + "</li>");
        response.getWriter().println("</ul>");

    }
}
