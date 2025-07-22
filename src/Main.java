import de.alexf99.TCP.TcpServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TcpServer tcpServer = new TcpServer();
        tcpServer.start(8002);
    }
}