package group10;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

public class Util {

    // Parsing of JSON payload based on: https://gist.github.com/gjtorikian/5171861

    /**
     * Checks if the head_commit parameter is present and its commit id is not null
     * (only the push event contains the head_commit parameter, thus it can be used
     * to check for a push event).
     *
     * @param payload event payload
     * @return true if the payload describes a push/commit event, else false
     */
    public static boolean isCommitEvent(JSONObject payload) {
        final String headCommit = "head_commit";
        if (payload.containsKey(headCommit)) {
            // If commit id of head_commit is not null, return true
            JSONObject jsonHeadCommit = (JSONObject) payload.get(headCommit);
            return jsonHeadCommit.get("id") != null;
        }
        return false;
    }

    /**
     * Gets the branch tied to the event.
     *
     * @param payload event payload
     * @return a String with the branch name of the event
     */
    public static String getBranch(JSONObject payload) {
        String refValue = (String) payload.get("ref");
        return refValue.split("/")[2];
        //com
    }

    /**
     * Gets the repository name tied to the event.
     *
     * @param payload event payload
     * @return a String with the name of the repository
     */
    public static String getRepositoryName(JSONObject payload) {
        JSONObject repository = (JSONObject) payload.get("repository");
        return (String) repository.get("name");
    }

    /**
     * Gets the address for cloning the repository using https.
     *
     * @param payload event payload
     * @return a String with the clone URL (https)
     */
    public static String getCloneURL(JSONObject payload) {
        JSONObject repository = (JSONObject) payload.get("repository");
        return repository.get("url") + ".git";
    }

    /**
     * Gets the BufferedReader contents as a String.
     *
     * @param in input BufferedReader
     * @return String containing the contents of in
     * @throws IOException if an I/O error occurs
     */
    public static String convertToString(BufferedReader in) throws IOException {
        StringBuilder builder = new StringBuilder();
        String readLine;
        while ((readLine = in.readLine()) != null) {
            builder.append(readLine);
        }
        return builder.toString();
    }

}
