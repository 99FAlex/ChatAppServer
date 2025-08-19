package de.alexf99;

import de.alexf99.TCP.TcpServer;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        TcpServer tcpServer = new TcpServer();
        DataInputStream in = new DataInputStream(System.in);
        File messageFile = new File("messages.txt");
        messageFile.createNewFile();

        /*Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try (PrintWriter out = new PrintWriter(new FileWriter("shutdown_log.txt", true))) {
                out.println("The program is shutting down at " + new java.util.Date());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));*/

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
            if (portString.equals("")){
                portString = "8001";
            }

            //write new line with \n
            configWriter.write("port=" + portString);
            configWriter.close();
            port = Integer.parseInt(portString);
        }

        String urlString = "http://checkip.amazonaws.com/";
        URL url = new URL(urlString);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            System.out.println("Current ip adress: " + br.readLine());
        }
        System.out.println("Server starting on port " + port);

        CompletableFuture<Void> future = readConsoleAsync();

        tcpServer.start(port);


    }

    public static void changePort(String port) throws IOException {
        File config = new File("config.txt");
        Scanner configReader = new Scanner(config);
        String portline = configReader.nextLine();
        configReader.close();
        FileWriter configWriter = new FileWriter("config.txt");

        String portConf = portline.split("=")[1];


        configWriter.write("port=" + port);
        configWriter.close();

    }

    private static final java.util.concurrent.ExecutorService executor = Executors.newCachedThreadPool();



    public static CompletableFuture<Void> readConsoleAsync() {
        return CompletableFuture.runAsync(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    while (true){
                        System.out.println("Bitte gib etwas ein:");
                        String line = reader.readLine();
                        if (line != null && line.equalsIgnoreCase("port")) {
                            System.out.println("Enter new port");
                            String port = reader.readLine();
                            changePort(port);
                            System.out.println("Please restart the server");
                            System.out.println("Stopping...");
                            Thread.sleep(1000);
                            System.exit(0);

                        }else if (line != null && line.equalsIgnoreCase("stop")){
                            System.exit(0);
                        }else if(line != null && line.equalsIgnoreCase("help")){
                            System.out.println(Colors.text_BRIGHT_BLUE + "List of Commands");
                            System.out.println(Colors.text_BRIGHT_BLUE + "port - to change the port");
                            System.out.println(Colors.text_BRIGHT_BLUE + "stop - stops the server");
                            System.out.println(Colors.text_BRIGHT_BLUE + "help - shows this menu" + Colors.text_RESET);

                        }else {
                            System.out.println(Colors.text_RED + "Command not found. Please user " + Colors.text_BRIGHT_BLUE + "help" + Colors.text_RED + " to see every command" + Colors.text_RESET);
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


        }, executor);
    }
}