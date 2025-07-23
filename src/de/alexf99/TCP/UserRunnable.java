package de.alexf99.TCP;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;

public class UserRunnable implements Runnable{

    private Socket clientSocket;
    private String username;
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
                if (username == null){
                    username = receivedData;
                }else {
                    TcpServer.clients.forEach((name, socket) -> {
                        try {
                            System.out.println(name);
                            String message = username + " --> " + receivedData;
                            socket.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
                            socket.getOutputStream().flush();
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    });
                }

            }
        }catch(IOException e){
            System.out.println("IOExeption for client " + Thread.currentThread().getName() + ": " + e.getMessage());
        }catch (Exception e){
            System.out.println("General Exception for client " + Thread.currentThread().getName() + ": " + e.getMessage());
        }finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()){
                    clientSocket.close();
                    System.out.println("Socket closed || " + Thread.currentThread().getName());
                }

            } catch (IOException e) {
                System.out.println("Error closing Connection : " + e.getMessage());
            }
            try {
                TcpServer.clients.remove(Thread.currentThread().getName());
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                System.out.println("Error thread joining : " + e.getMessage());
            }

        }
    }
}
