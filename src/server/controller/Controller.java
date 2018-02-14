package server.controller;

import server.model.Game;
import server.model.Point;

public class Controller {
    private static Controller instance;
    private String resume;
    private Receiver receiver;
    private Game game;

    private Controller() {
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getResume() {
        return resume;
    }

    public void sendFileData(String[][] cellsResult) {
        receiver.receiver(cellsResult);
    }

    public void initField() {
        game.initField();
    }

    public void nextStep() {
        game.doStep();
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public void setUserStep(Point userShot) {
        game.setUserStep(userShot);
    }

    public interface Receiver {
        void receiver(String[][] data);
    }
}