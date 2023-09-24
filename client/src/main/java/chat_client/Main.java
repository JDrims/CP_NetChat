package chat_client;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String path = new File("").getAbsolutePath();
        SettingsWorker settingsWorker = new SettingsWorker(path + "\\Setting.xml");
        Client client = new Client();
        client.setLogWorker(new LogWorker(path + "\\clientLogFile.log"));
        client.setFormatForDateNow(new SimpleDateFormat(settingsWorker.valueFromTitle("DateTimeFormate")));
        String host = settingsWorker.valueFromTitle("host");
        int port = Integer.parseInt(settingsWorker.valueFromTitle("port"));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!client.getActive()) {
                System.out.println("Enter name : ");
            }
            String command = scanner.next();
            if (command.toLowerCase().equals("/exit")) {
                client.disconnect();
            } else {
                if (client.getActive()) {
                    client.sendMessage(command);
                } else {
                    client.connect(command, host, port);
                }
            }
        }
    }
}