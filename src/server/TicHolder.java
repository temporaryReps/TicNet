package server;

import server.model.DataContainer;
import server.model.Game;
import server.model.Point;

import java.io.*;
import java.net.Socket;

/**
 * Holder connection with current user
 */
public class TicHolder implements Runnable {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private Game game;

    public TicHolder(Socket socket) {
        this.socket = socket;
        game = new Game();
        new Thread(this).start();
    }

    @Override
    public void run() {
        initConnection(socket);
        interaction();
    }

    private void initConnection(Socket socket) {
        try {
            outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * lead interaction with user until the socket is connected
     */
    private void interaction() {
        DataContainer gameField = game.initField();

        while (true) {
            try {
                outputStream.writeObject(gameField);
                outputStream.flush();
                outputStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                return;
            }

            try {
                int[] pointData = (int[]) inputStream.readObject();
                game.doStep(new Point(pointData[0], pointData[1]));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            gameField = game.getFieldData();
        }
    }
}