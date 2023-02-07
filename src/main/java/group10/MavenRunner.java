package group10;

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Collections;

public class MavenRunner {

    private String buildPath;

    public MavenRunner(String buildPath) {
        this.buildPath = buildPath;
    }

    public boolean runBuildAndTests() throws MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File("./pom.xml"));
        request.setGoals(Collections.singletonList("test"));

        Invoker invoker = new DefaultInvoker();
        invoker.setLocalRepositoryDirectory(new File(this.buildPath));

        InvocationResult result = invoker.execute(request);

        if (result.getExitCode() != 0) {
            return false;
        }

        return true;
    }

}
