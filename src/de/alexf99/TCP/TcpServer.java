package de.alexf99.TCP;

import java.io.IOException;
import java.net.*;
import java.util.*;


public class TcpServer {

    public static Map<String, Socket> clients = new HashMap<>();


    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);


        while (true){
            try {
                Socket socket = serverSocket.accept();
                Thread client = new Thread(new UserRunnable(socket));
                client.start();
                System.out.println("client connected");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }



    }



}
