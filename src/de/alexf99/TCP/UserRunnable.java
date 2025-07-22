package de.alexf99.TCP;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;

public class UserRunnable implements Runnable{

    private Socket clientSocket;
    UserRunnable(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            TcpServer.clients.put(Thread.currentThread().getName(), clientSocket);
            OutputStream out = clientSocket.getOutputStream();
            BufferedReader inReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            InputStream in = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                String receivedData = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                System.out.println(Thread.currentThread().getName() + " || " +receivedData);
                TcpServer.clients.forEach((name, socket) -> {
                    try {
                        socket.getOutputStream().write(receivedData.getBytes(StandardCharsets.UTF_8));
                        socket.getOutputStream().flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Test");
            System.out.println(e.getMessage());        }

    }
}
