package group10;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class LogWriter {
    File file;

    public LogWriter() {
        file = new File("history/" + new Date() + ".html");
    }

    public void write(String message) {
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.append("<p>" + message + "</p>");
            writer.close();
        } catch (Exception e) {
            return;
        }
    }

}
