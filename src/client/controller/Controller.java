package client.controller;

import server.model.DataContainer;

public class Controller {
    private static Controller instance;
    private Updatable window; // GUI class
    private Receiver receiver; // receiver of data from GUI


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

    /**
     * parse data form DataContainer ant send them to GUI class
     * @param data from server
     */
    public void setData(DataContainer data) {
        String[][] field = data.getFieldData();
        String report = data.getReport();
        window.update(field, report);
    }

    /**
     * send point which was selected by user
     * @param point array of two elements x and y coordinate
     */
    public void doShoot(int[] point) {
        receiver.newShot(point);
    }

    public void setWindow(Updatable window) {
        this.window = window;
    }

    /**
     * receiver data from user UI
     */
    public interface Receiver {
        void newShot(int[] shot);
    }

    /**
     * update UI with new data
     */
    public interface Updatable {
        void update(String[][] field, String report);
    }
}