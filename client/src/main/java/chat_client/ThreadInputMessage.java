package chat_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

public class ThreadInputMessage extends Thread {
    private Socket socket;
    private Client client;

    public ThreadInputMessage(Client client, Socket socket) {
        this.client = client;
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader input = null;
        try {
            String message;
            input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            while ((message = input.readLine()) != null) {
                System.out.println(client.getFormatForDateNow().format(new Date()) + " " + message);
                client.getLogWorker().logWrite(client.getFormatForDateNow().format(new Date()) + " " + message);
            }
        } catch (IOException e) {
            try {
                input.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            System.out.println(client.getFormatForDateNow().format(new Date()) + " " + client.getName() + "(you) : " + " Disconnect from server");
            client.getLogWorker().logWrite(client.getFormatForDateNow().format(new Date()) + " " + client.getName() + "(you) : " + " Disconnect from server");
            System.exit(0);
        }
    }
}
