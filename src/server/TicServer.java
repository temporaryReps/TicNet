package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TicServer {
    private static final int PORT = 8080;

    public void connect() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new TicHolder(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}