package client;

import client.view.GameWindow;

public class Main {
    public static void main(String[] args) {
        new GameWindow().init();
        new TicClient();
    }
}