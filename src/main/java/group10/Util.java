package group10;

import org.json.simple.JSONObject;

public class Util {

    // Parsing of JSON payload based on: https://gist.github.com/gjtorikian/5171861

    /**
     * Checks if the head_commit parameter is present and its commit id is not null
     * (Only the push event contains the head_commit parameter, thus it can be used
     * to check for a push event)
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

}
