package client;

import client.controller.Controller;
import client.model.Point;

import java.io.*;
import java.net.Socket;

public class TicClient implements Controller.Receiver {
    private static final String SITE = "localhost";
    private static final int PORT = 8060;
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
    public void newShot(Point shot) {
        interaction(shot);
    }

    private void initConnection() {
        System.out.println("init");
        try {
            socket = new Socket(SITE, PORT);
            System.out.println(socket);
            inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            System.out.println(inputStream);
            outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            System.out.println(outputStream);
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
        System.out.println("end");
    }

    private void interaction(Point shot) {
            try {
                String[][] fieldData = (String[][]) inputStream.readObject();
                controller.setData(fieldData);

                System.out.println(outputStream);
                outputStream.writeObject(shot);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}