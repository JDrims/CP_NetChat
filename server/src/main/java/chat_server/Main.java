package chat_server;

import java.io.File;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) {
        String path = new File("").getAbsolutePath();
        SettingsWorker settingsWorker = new SettingsWorker(path + "\\Setting.xml");
        Server server = new Server();
        server.setLogWorker(new LogWorker(path + "\\serverLogFile.log"));
        server.setFormatForDateNow(new SimpleDateFormat(settingsWorker.valueFromTitle("DateTimeFormate")));
        server.start(Integer.parseInt(settingsWorker.valueFromTitle("port")));
    }
}