package server.model;

import server.controller.Controller;

public class Game {
    private Controller controller;
    private Field field;
    private Computer computer;
    private User user;

    public Game() {
        controller = Controller.getInstance();
        user = new User();
        field = new Field();
        computer = new Computer();
        field.init();
    }

    public void doStep() {
        field.shoot(user.getShootPoint(), Field.Type.X);
//        field.getFieldData();
        if (field.whoIsWinner() == Field.Type.X) {
            controller.setResume("Победил " + Field.Type.X);
            field.getFieldData();
            return;
        }

        field.shoot(computer.getShootPoint(), Field.Type.O);
        if (field.whoIsWinner() == Field.Type.O) {
            controller.setResume("Победил " + Field.Type.O);
        }
        field.getFieldData();
    }

    public void initField() {
        field.getFieldData();
    }

    public void setUserStep(Point shot) {
        user.setShotPoint(shot);
    }
}
