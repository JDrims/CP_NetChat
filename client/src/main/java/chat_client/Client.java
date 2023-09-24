package chat_client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    private boolean isActive = false;
    private Socket socket;
    private LogWorker logWorker;
    private SimpleDateFormat formatForDateNow;
    private String name;

    public void connect(String name, String host, int port) {
        isActive = true;
        this.name = name;
        try {
            InetAddress ipAddress = InetAddress.getByName(host);
            int usePort = port;
            if (!(port > 0) & !(port < 65536)) {
                usePort = 8089;
            }
            socket = new Socket(ipAddress, usePort);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            System.out.println(formatForDateNow.format(new Date()) + " " + name + "(you) : " + " Connect to server");
            logWorker.logWrite(formatForDateNow.format(new Date()) + " " + name + "(you) : " + " Connect to server");
            writer.println(name);
            ThreadInputMessage threadInputMessage = new ThreadInputMessage(this, socket);
            threadInputMessage.start();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);
            System.out.println(formatForDateNow.format(new Date()) + " " + name + "(you) : " + message);
            logWorker.logWrite(formatForDateNow.format(new Date()) + " " + name + "(you) : " + message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLogWorker(LogWorker logWorker) {
        this.logWorker = logWorker;
    }

    public void setFormatForDateNow(SimpleDateFormat simpleDateFormat) {
        this.formatForDateNow = simpleDateFormat;
    }

    public void disconnect() {
        try {
            isActive = false;
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getActive() {
        return this.isActive;
    }

    public SimpleDateFormat getFormatForDateNow() {
        return this.formatForDateNow;
    }

    public LogWorker getLogWorker() {
        return this.logWorker;
    }

    public String getName() {
        return this.name;
    }
}
