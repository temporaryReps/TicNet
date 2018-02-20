package server;

import server.model.DataContainer;
import server.model.Game;
import server.model.GameContainer;
import server.model.Point;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Holder connection with current user
 */
public class TicHolder implements Runnable {
    private static List<TicHolder> holders =
            Collections.synchronizedList(new ArrayList<>()); // list of users' holder
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Game game;
    private int userNumber;

    public TicHolder(Socket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    @Override
    public void run() {
        holders.add(this);
        initConnection(socket);
        handleUserAnswer();
    }

    private void initConnection(Socket socket) {
        try {
            outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            outputStream.writeObject(0);
            outputStream.flush();
            outputStream.reset();
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
     * wail user's answer. Who will be his opponent (computer or human)
     */
    private void handleUserAnswer() {
        boolean isComputer = true;
        try {
            isComputer = (Boolean) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isComputer) {
            game = new Game(); // new game with computer
            interaction(true);
        } else {
            game = GameContainer.getGameInstance(); // the same game for two users
            initUsersInteraction();
        }
    }

    /**
     * lead interaction with user until the socket is connected
     */
    private void interaction(boolean isComputer) {
        DataContainer gameField;
        gameField = game.initField();

        while (true) {
            try {
                if (isComputer) {
                    sendData(gameField, this);
                } else {
                    sendDataForEach(gameField);
                }
            } catch (IOException e) {
                TicServer.setUsersCountDecrement();
                holders.add(this);
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
                if (isComputer) {
                    // game with computer
                    game.doStep(new Point(pointData[0], pointData[1]));
                } else {
                    // game with user
                    game.doStep(new Point(pointData[0], pointData[1]), userNumber);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            gameField = game.getFieldData();
        }
    }

    /**
     * write resume which is depended on number of user and launch interaction
     */
    private void initUsersInteraction() {
        if (TicServer.getUsersCount() < 2) {
            userNumber = Game.FIRST_USER;
            game.setResume("Ждём второго игрока");
        } else {
            userNumber = Game.SECOND_USER;
            game.setResume("Начинаем игру");
        }

        interaction(false);
    }

    /**
     * send date to both users
     *
     * @param data which send to users
     * @throws IOException
     */
    private void sendDataForEach(DataContainer data) throws IOException {
        for (TicHolder holder : holders) {
            sendData(data, holder);
        }
    }

    /**
     * send data to user
     *
     * @param data   which send to user
     * @param holder current instance of TicHolder for user
     * @throws IOException
     */
    private void sendData(DataContainer data, TicHolder holder) throws IOException {
        holder.outputStream.writeObject(data);
        holder.outputStream.flush();
        holder.outputStream.reset();
    }
}