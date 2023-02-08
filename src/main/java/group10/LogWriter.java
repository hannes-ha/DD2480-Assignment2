package group10;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogWriter {
    File file;

    public LogWriter() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd_HH:mm:ss");
        String filename = date.format(formatter);

        file = new File("history/" + filename + ".html");
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.append("<ul>");
            writer.close();
        } catch (Exception e) {
            return;
        }
    }

    public void write(String message) {
        try {
            FileWriter writer = new FileWriter(file, true);

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String time = date.format(formatter);

            writer.append("<p>" + "[" + time + "] " + message + "</p>");
            writer.close();
        } catch (Exception e) {
            return;
        }
    }

    public void close() {
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.append("</ul>");
            writer.close();
        } catch (Exception e) {
            return;
        }
    }

}
