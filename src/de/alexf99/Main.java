package de.alexf99;

import de.alexf99.TCP.TcpServer;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Main {
    public static final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

    public static void main(String[] args) throws IOException, InterruptedException {
        TcpServer tcpServer = new TcpServer();
        DataInputStream in = new DataInputStream(System.in);
        File messageFile = new File("messages.txt");
        messageFile.createNewFile();

        int port = 8001;

        if (Files.exists(Path.of("config.txt"))){
            try {
                File config = new File("config.txt");
                Scanner configReader = new Scanner(config);
                String portline = configReader.nextLine();
                configReader.close();
                String portConf = portline.split("=")[1];

                System.out.println("Port read from Config as " + portConf);
                port = Integer.parseInt(portConf);
            }catch (Exception e){
                System.out.println(Colors.text_BRIGHT_RED + "Error with config.txt");
                System.out.println(Colors.text_BRIGHT_RED + "Please check if everythin is ok");
                System.out.println(Colors.text_BRIGHT_RED + "then restart the server");
                System.out.println(Colors.text_BRIGHT_RED + "If you cant unterstand what is wrong just delete the config.txt");
                System.exit(1);
            }


        }else {
            FileWriter configWriter = new FileWriter("config.txt");

            System.out.println("Enter the port for running the server(default 8001): ");
            String portString = in.readLine();
            //write new line with \n
            configWriter.write("port=" + portString);
            configWriter.close();
            port = Integer.parseInt(portString);
        }

        System.out.println("Server starting on port " + port);

        tcpServer.start(port);
        while(true) {
            queue.take().run();
        }
    }

    public static void addMessage(String msg) throws IOException {


    }
}