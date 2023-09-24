package chat_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {
    private static List<ClientSock> clientSocks;
    private LogWorker logWorker;
    private SimpleDateFormat formatForDateNow;

    public void start(int port) {
        clientSocks = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println(formatForDateNow.format(new Date()) + " Server started");
            logWorker.logWrite(formatForDateNow.format(new Date()) + " Server started");
            while (true) {
                Socket client = serverSocket.accept();
                ClientSock clientSock = new ClientSock(this, client);
                clientSocks.add(clientSock);
                clientSock.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(formatForDateNow.format(new Date()) + " Server off");
            logWorker.logWrite(formatForDateNow.format(new Date()) + " Server off");
        }
    }

    public void setLogWorker(LogWorker logWorker) {
        this.logWorker = logWorker;
    }

    public void setFormatForDateNow(SimpleDateFormat simpleDateFormat) {
        this.formatForDateNow = simpleDateFormat;
    }

    public SimpleDateFormat getFormatForDateNow() {
        return this.formatForDateNow;
    }

    public LogWorker getLogWorker() {
        return this.logWorker;
    }

    public void broadcastMessage(ClientSock client, String message) {
        for (ClientSock clientSock : clientSocks) {
            if (!client.equals(clientSock)) {
                clientSock.sendMessage(message);
            }
        }
    }

    public void removeSocket(ClientSock clientSock) {
        clientSocks.remove(clientSock);
    }
}
