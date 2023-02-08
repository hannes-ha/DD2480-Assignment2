package group10;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.json.simple.JSONObject;

public class UtilTest {

    /**
     * Asserts true
     * Should return true since the payload contains a head_commit property
     * and its commit id is not null
     */
    @Test
    public void testUtil_isCommitEvent_Positive() {
        JSONObject payload = new JSONObject();
        JSONObject headCommitObject = new JSONObject();

        headCommitObject.put("id", "b383e670849aa57543c54c0c074ea6362bdaae99");
        payload.put("head_commit", headCommitObject);

        boolean result = Util.isCommitEvent(payload);
        assertFalse(result);
    }

    /**
     * Asserts false
     * Should return false since the payload contains a head_commit property
     * but its commit id is null
     */
    @Test
    public void testUtil_isCommitEvent_Negative_1() {
        JSONObject payload = new JSONObject();
        JSONObject headCommitObject = new JSONObject();

        headCommitObject.put("id", null);
        payload.put("head_commit", headCommitObject);

        boolean result = Util.isCommitEvent(payload);
        assertFalse(result);
    }


    /**
     * Asserts false
     * Should return false since the payload does not contain
     * a head_commit property
     */
    @Test
    public void testUtil_isCommitEvent_Negative_2() {
        JSONObject payload = new JSONObject();
        JSONObject headCommitObject = new JSONObject();

        headCommitObject.put("id", "182391");
        payload.put("some_prop", headCommitObject);

        boolean result = Util.isCommitEvent(payload);
        assertFalse(result);
    }

    /**
     * Assert equals (expected = "main")
     * Should return "main" since the payload refers
     * to the main branch
     */
    @Test
    public void testUtil_getBranch() {
        JSONObject payload = new JSONObject();
        payload.put("ref", "refs/heads/main");

        String result = Util.getBranch(payload);
        assertEquals("main", result);
    }

    /**
     * Assert equals (expected = "test-repo-name")
     * Should return "test-repo-name" since the payload is tied
     * to the repository with name "test-repo-name"
     */
    @Test
    public void testUtil_getRepositoryName() {
        JSONObject payload = new JSONObject();
        JSONObject repository = new JSONObject();

        repository.put("name", "test-repo-name");
        payload.put("repository", repository);

        String result = Util.getRepositoryName(payload);
        assertEquals("test-repo-name", result);
    }

    /**
     * Assert equals (expected = "https://github.com/hannes-ha/DD2480-Assignment2.git")
     * Should return "https://github.com/hannes-ha/DD2480-Assignment2.git" since payload
     * has repository url "https://github.com/hannes-ha/DD2480-Assignment2"
     */
    @Test
    public void testUtil_getCloneURL() {
        JSONObject payload = new JSONObject();
        JSONObject repository = new JSONObject();

        repository.put("url", "https://github.com/hannes-ha/DD2480-Assignment2");
        payload.put("repository", repository);

        String result = Util.getCloneURL(payload);
        assertEquals("https://github.com/hannes-ha/DD2480-Assignment2.git", result);
    }

}
