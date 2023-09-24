package chat_server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogWorker {
    private String logFileName;

    public LogWorker(String logFileName) {
        this.logFileName = logFileName;
    }

    public void logWrite(String line) {
        File file = new File(this.logFileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(line + "\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
