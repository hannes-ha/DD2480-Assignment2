package group10;

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Collections;

import group10.ContinuousIntegrationServer.BuildStatus;

/**
 * Class for handling running of Maven.
 */
public class MavenRunner {

    private final Invoker invoker;

    /**
     * Creates an object for handling running of Maven.
     *
     * @param buildPath the path of the maven project to be built and tested
     */
    public MavenRunner(String buildPath) {
        invoker = new DefaultInvoker();
        invoker.setLocalRepositoryDirectory(new File(buildPath));
    }

    /**
     * Runs mvn compile in the specified buildPath and reports
     * the results.
     *
     * @return the BuildStatus - BUILD_FAILED or BUILD_SUCCEEDED
     */
    public BuildStatus runMvnCompile() {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setGoals(Collections.singletonList("compile"));
        request.setBatchMode(true);
        request.setQuiet(true);

        try {
            InvocationResult result = invoker.execute(request);

            if (result.getExitCode() != 0) {
                return BuildStatus.BUILD_FAILED;
            }
        } catch (MavenInvocationException e) {
            return BuildStatus.BUILD_FAILED;
        }
        return BuildStatus.BUILD_SUCCEEDED;
    }

    /**
     * Runs mvn test in the specified buildPath and reports
     * the results.
     *
     * @return the BuildStatus - TESTS_FAILED or TESTS_SUCCEEDED
     */
    public BuildStatus runMvnTest() {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setGoals(Collections.singletonList("test"));
        request.setBatchMode(true);
        request.setQuiet(true);

        try {
            InvocationResult result = invoker.execute(request);

            if (result.getExitCode() != 0) {
                return BuildStatus.TESTS_FAILED;
            }
        } catch (MavenInvocationException e) {
            return BuildStatus.TESTS_FAILED;
        }
        return BuildStatus.TESTS_SUCCEEDED;
    }
}
