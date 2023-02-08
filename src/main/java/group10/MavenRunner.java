package group10;

import org.apache.maven.shared.invoker.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import group10.ContinuousIntegrationServer.BuildStatus;

/**
 * Class for handling running of Maven.
 */
public class MavenRunner {

    private final Invoker invoker;

    private static final String buildLogFileLocation = "./buildlog.txt";

    /**
     * Creates an object for handling running of Maven.
     *
     * @param buildPath the path of the maven project to be built and tested
     */
    public MavenRunner(String buildPath) {
        invoker = new DefaultInvoker();
        invoker.setLocalRepositoryDirectory(new File(buildPath));

        try {
            PrintStream out = new PrintStream(new FileOutputStream(buildLogFileLocation));
            InvocationOutputHandler outputHandler = new PrintStreamHandler(out, false);
            invoker.setOutputHandler(outputHandler);
        } catch (FileNotFoundException e) {
            System.out.println("Something went wrong while creating the temporary buildlog.");
        }

    }

    /**
     * Gets the full maven output from the build process.
     *
     * @return an ArrayList<String> where each element is a line from the output
     */
    public ArrayList<String> getBuildLogs() {
        ArrayList<String> lines = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(buildLogFileLocation))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file at " + buildLogFileLocation);
        } catch (IOException e) {
            System.out.println("Error while reading from build log file.");
        }

        return lines;
    }

    /**
     * Runs mvn compile in the specified buildPath and reports
     * the results.
     *
     * @return the BuildStatus - BUILD_FAILED or BUILD_SUCCEEDED
     */
    public BuildStatus runMvnCompile() {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( "./build/pom.xml" ) );
        request.setGoals(Collections.singletonList("compile"));
        request.setBatchMode(true);
        //request.setQuiet(true);

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
        request.setPomFile( new File( "./build/pom.xml" ) );
        request.setGoals(Collections.singletonList("test"));
        request.setBatchMode(true);
        //request.setQuiet(true);

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
