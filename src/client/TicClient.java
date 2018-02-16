package client;

import client.controller.Controller;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class TicClient implements Controller.Receiver {
    private static final String SITE = "localhost";
    private static final int PORT = 8080;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Controller controller;

    public TicClient() {
        controller = Controller.getInstance();
        controller.setReceiver(this);
        initConnection();
    }

    @Override
    public void newShot(int[] shot) {
        interaction(shot);
    }

    private void initConnection() {
        System.out.println("init");
        try {
            socket = new Socket(SITE, PORT);
            System.out.println(socket);
            outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            outputStream.flush();
            System.out.println(outputStream);
            inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            System.out.println(inputStream);

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
        readData();
        System.out.println("end");
    }

    private void interaction(int[] shot) {
        try {
            System.out.println(outputStream);
            outputStream.writeObject(shot);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        readData();
    }

    private void readData() {
        String[][] fieldData = null;
        try {
            System.out.println("pre-read");
            fieldData = (String[][]) inputStream.readObject();
            System.out.println(Arrays.deepToString(fieldData));
            System.out.println("post-read");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.setData(fieldData);
    }
}