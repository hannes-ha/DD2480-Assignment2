package group10;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

/**
 * Class for handling commit statuses
 */
public class CommitStatusHandler {
    private String token;

    /**
     * Constructor for CommitStatusHandler
     * Gets the github token from the .env file
     */
    public CommitStatusHandler() {
        Dotenv dotenv = Dotenv.load();
        token = dotenv.get("GITHUB_TOKEN");
        if (token == null) {
            throw new DotenvException("Create a .env file with a GITHUB_TOKEN variable");
        }
    }

    /**
     * Gets the status of a commit
     * @param commitHash the SHA hash of the commit
     * @return the status of the commit
     */
    public String getStatus(String commitHash) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(
                        "https://api.github.com/repos/hannes-ha/DD2480-Assignment2/commits/" + commitHash + "/status"))
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .GET()
                .build();

        String status = "";
        try {
            HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject obj = (JSONObject) new JSONParser().parse(res.body());
            System.out.println(res);
            status = obj.get("state").toString();

        } catch (Exception e) {
            return "";

        }
        return status;
    }

    /**
     * Sets the status of a commit
     * @param commitHash the SHA hash of the commit
     * @param status the status to set
     */
    public void setStatus(String commitHash, String status) {
        String[] allowedStatuses = { "success", "failure", "pending", "error" };
        if (!Arrays.asList(allowedStatuses).contains(status)) {
            throw new IllegalArgumentException(
                    "Invalid status, allowed statuses are: " + Arrays.toString(allowedStatuses));
        }

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(
                URI.create("https://api.github.com/repos/hannes-ha/DD2480-Assignment2/statuses/" + commitHash))
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .POST(BodyPublishers.ofString("{\"state\":\"" + status + "\"}"))
                .build();

        try {
            client.send(request, BodyHandlers.ofString());
        } catch (Exception e) {
            return;
        }
    }

}