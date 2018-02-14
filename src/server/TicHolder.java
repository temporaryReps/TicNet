package server;

import server.controller.Controller;
import server.model.Point;

import java.io.*;
import java.net.Socket;

public class TicHolder implements Runnable, Controller.Receiver {
    private Socket socket;
    private Controller controller;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public TicHolder(Socket socket) {
        this.socket = socket;
        controller = Controller.getInstance();
        controller.setReceiver(this);

        new Thread(this).start();
    }

    @Override
    public void run() {
        initConnection(socket);
    }

    @Override
    public void receiver(String[][] fieldData) {
        interaction(fieldData);
    }

    private void initConnection(Socket socket) {
        try {
            outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        controller.initField();
    }

    private void interaction(String[][] fieldData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(fieldData);
                    outputStream.writeObject(fieldData);
                    Point clientStep = (Point) inputStream.readObject();
                    controller.setUserStep(clientStep);
                    controller.nextStep();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}