package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Occupy the port and wait user
 */
public class TicServer {
    private static final int PORT = 8080;

    /**
     * wait connection with user and give instance of TicHolder to him
     */
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