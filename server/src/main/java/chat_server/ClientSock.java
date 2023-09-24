package chat_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class ClientSock extends Thread {
    private String name = null;
    private final Socket clientSocket;
    private final Server server;

    public ClientSock(Server server, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.name = input.readLine();
            System.out.println(server.getFormatForDateNow().format(new Date()) + " " + this.name + " " + clientSocket.getInetAddress().toString() + " : Connected to server");
            server.getLogWorker().logWrite(server.getFormatForDateNow().format(new Date()) + " " + this.name + " : Connected to server");
            server.broadcastMessage(this, this.name + " : Connected to server");
            String message;
            while ((message = input.readLine()) != null) {
                System.out.println(server.getFormatForDateNow().format(new Date()) + " " + this.name + " : " + message);
                server.getLogWorker().logWrite(server.getFormatForDateNow().format(new Date()) + " " + this.name + " : " + message);
                server.broadcastMessage(this, this.name + " : " + message);
            }
        } catch (IOException e) {
            try {
                input.close();
                clientSocket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            server.removeSocket(this);
            server.broadcastMessage(this, this.name + " : Disconnected to server");
            System.out.println(server.getFormatForDateNow().format(new Date()) + " " + this.name + " : Disconnected to server");
            server.getLogWorker().logWrite(server.getFormatForDateNow().format(new Date()) + " " + this.name + " : Disconnected to server");
        }
    }

    public void sendMessage(String message) {
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
