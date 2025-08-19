package de.alexf99.TCP;

import de.alexf99.Colors;
import de.alexf99.Main;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;


public class TcpServer {

    public static Map<String, Socket> clients = new HashMap<>();


    public void start(int port) throws IOException {
        System.out.println("ChatApp server started");
        System.out.println("Now accepting user");

        ServerSocket serverSocket = new ServerSocket(port);


        while (true){
            try {
                Socket socket = serverSocket.accept();
                Thread client = new Thread(new UserRunnable(socket));
                client.start();
                System.out.println(Colors.text_BLUE + "Client connected" + Colors.text_RESET);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }



    }



}
