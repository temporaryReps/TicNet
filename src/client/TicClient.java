package client;

import client.controller.Controller;
import server.model.DataContainer;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class TicClient implements Controller.Receiver {
    private static final String SITE = "localhost";
    private static final int PORT = 8080;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Controller controller; // for interaction with UI class

    public TicClient() {
        controller = Controller.getInstance();
        controller.setReceiver(this);
        initConnection();
    }

    @Override
    public void newShot(int[] shot) {
        interaction(shot);
    }

    @Override
    public void setGamer(boolean isComputer) {
        try {
            outputStream.writeObject(isComputer);
            outputStream.flush();
            try {
                inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        readData();
    }

    private void initConnection() {
        try {
            socket = new Socket(SITE, PORT);
            outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            outputStream.flush();
            inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * send point which was selected by user to server
     *
     * @param shot array of two elements x and y coordinate
     */
    private void interaction(int[] shot) {
        try {
            outputStream.writeObject(shot);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read data from server and send them to controller
     * within a separate thread
     */
    private void readData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    DataContainer fieldData;
                    try {
                        fieldData = (DataContainer) inputStream.readObject();
                        System.out.println(Arrays.deepToString(fieldData.getFieldData()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    controller.setData(fieldData);
                }
            }
        }).start();
    }
}