package group10;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Date;
import java.util.Properties;
import org.json.simple.JSONObject;

public class MailHandler{

    public void emailResults (String email, String results){
        //Reformation
        String resultsRef = reformatResult(results);

        String to;
        if(email.length() != 0){
            to = email;
        }else{
            to = "dd2480test@gmail.com";
        }

        MailHandler m = new MailHandler();
        m.sendMail(to, resultsRef);
    }

    public void emailResultsJsonObj (JSONObject payload, String results){
        //Reformation
        String resultsRef = reformatResult(results);

        String to = getMailFromPayload(payload);

        MailHandler m = new MailHandler();
        m.sendMail(to, resultsRef);
    }

    private String reformatResult(String results) {
        String [] r = results.split(" ");
        //Format: BUILD TESTS CLONE_SUCCESS
        StringBuilder sb = new StringBuilder();
        for (String s: r) {
            sb.append(s).append("\n");
        }

        return sb.toString();
    }

    private String getMailFromPayload(JSONObject payload){
        JSONObject jsonHeadCommit = (JSONObject) payload.get("head_commit");
        JSONObject jsonAuthor = (JSONObject) jsonHeadCommit.get("author");
        return (String) jsonAuthor.get("email");
    }

    private void sendMail(String receiver, String results){
        String host = "smtp.gmail.com";
        String port = "587";

       try {
           Date date = new Date();
           final String from = "dd2480test@gmail.com";
           BufferedReader br = new BufferedReader(new FileReader("passw.txt"));
           final String password = br.readLine();

           Properties props = System.getProperties();

           props.put("mail.smtp.host", host);
           props.put("mail.smtp.port", port);
           props.put("mail.smtp.auth", "true"); //turn on authentication
           props.put("mail.smtp.starttls.enable", "true"); //TLS

           Session session = Session.getInstance(props,
                   new javax.mail.Authenticator() {
                       protected PasswordAuthentication getPasswordAuthentication() {
                           return new PasswordAuthentication(from, password);
                       }
                   });

           MimeMessage msg = new MimeMessage(session);
           //set message headers
           msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
           msg.addHeader("format", "flowed");
           msg.addHeader("Content-Transfer-Encoding", "8bit");

           msg.setFrom(new InternetAddress(from, "NoReply-JD"));
           msg.setSubject("Build result -" + date, "UTF-8");
           msg.setText(results, "UTF-8");
           msg.setSentDate(date);
           msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver, false));
           //send
           javax.mail.Transport.send(msg);

       }catch(Exception e){
            System.out.println(e.getMessage());
       }
    }
}