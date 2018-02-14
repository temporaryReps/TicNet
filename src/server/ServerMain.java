package server;

import server.controller.Controller;
import server.model.Game;

public class ServerMain {
    public static void main(String[] args) {
        new TicServer().connect();
        Controller controller = Controller.getInstance();
        controller.setGame(new Game());
    }
}
