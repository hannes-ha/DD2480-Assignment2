package group10;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for handling logging.
 */
public class LogWriter {
    File file;

    /**
     * Constructor -creates an instance of LogWriter
     * Creates a .html file for logging under directory "history" with the
     * timestamp as name
     */
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

    /**
     * Writes a message into the log
     *
     * @param message the message to be added into the log
     */
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

    /**
     * Closes the logging
     */
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
