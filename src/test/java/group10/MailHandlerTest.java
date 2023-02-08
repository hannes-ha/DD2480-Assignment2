package group10;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.json.simple.JSONObject;


public class MailHandlerTest
{
    @Test
    public void testSendString()
    {
        MailHandler m = new MailHandler();
        m.emailResults("dd2480test@gmail.com", "BUILD_SUCCESS TESTS_SUCCESS CLONE_SUCCESS");

        assertTrue(true);
    }
    @Test
    public void testSendJson()
    {
        JSONObject payload = new JSONObject();
        JSONObject headCommitObject = new JSONObject();
        payload.put("head_commit", headCommitObject);
        JSONObject authorObject = new JSONObject();
        headCommitObject.put("author", authorObject);
        authorObject.put("email", "dd2480test@gmail.com");

        MailHandler mj = new MailHandler();
        mj.emailResultsJsonObj(payload, "BUILD_SUCCESS TESTS_SUCCESS CLONE_SUCCESS");
        assertTrue( true );
    }
}
