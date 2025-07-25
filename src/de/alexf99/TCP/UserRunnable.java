package de.alexf99.TCP;



import de.alexf99.Colors;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


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
            InputStream in = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                String receivedData = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                if (username == null){
                    username = receivedData;
                }else {
                    System.out.println(Colors.text_RESET + username + " --> " + receivedData);
                    TcpServer.clients.forEach((name, socket) -> {
                        try {
                            String message = username + " --> " + receivedData;
                            File messageFile = new File("messages.txt");

                            if (messageFile.exists()){
                                Scanner configReader = new Scanner(messageFile);
                                if (configReader.hasNextLine()){
                                    FileWriter messageWriter = new FileWriter("messages.txt");

                                    List<String> msg = new ArrayList<>();
                                    while(configReader.hasNextLine()){
                                        msg.add(configReader.nextLine());
                                    }

                                    configReader.close();



                                    //write new line with \n
                                    for (String data : msg){
                                        messageWriter.write(data + "\n");

                                    }
                                    messageWriter.write("["+ LocalDate.now() + " | " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) +"]" +message);
                                    messageWriter.close();
                                }else {
                                    FileWriter messageWriter = new FileWriter("messages.txt");


                                    //write new line with \n
                                    messageWriter.write("["+ LocalDate.now() + " | " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) +"]" +message);
                                    messageWriter.close();
                                }
                            }

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
