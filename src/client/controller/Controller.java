package client.controller;

import client.model.Point;

public class Controller {
    private static Controller instance;
    private Updatable window;
    private Receiver receiver;


    private Controller() {
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public void setData(String[][] data) {
        window.update(data);
    }

    public void doShoot(Point point) {
        receiver.newShot(point);
    }

    public void setWindow(Updatable window) {
        this.window = window;
    }

    public interface Receiver {
        void newShot(Point shot);
    }

    public interface Updatable {
        void update(String[][] data);
    }
}
