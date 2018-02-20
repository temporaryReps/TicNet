package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Occupy the port and wait user
 */
public class TicServer {
    private static int usersCount;
    private static final int PORT = 8080;

    public static void setUsersCountDecrement() {
        usersCount--;
        System.out.println(usersCount);
    }

    public static int getUsersCount() {
        return usersCount;
    }

    /**
     * wait connection with user and give instance of TicHolder to him
     */
    public void connect() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();

                if (usersCount >= 2) {
                    socket.close();
                } else {
                    usersCount++;
                }
                System.out.println(usersCount);

                new TicHolder(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}