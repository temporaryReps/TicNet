package server.model;

import server.controller.Controller;

public class Field {
    public static final int SIZE = 3;

    public enum Type {
        X, O, NONE
    }

    private Type[][] cells = new Type[SIZE][SIZE];
    private String[][] cellsResult = new String[SIZE][SIZE];
    private Controller controller = Controller.getInstance();

    public void init() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = Type.NONE;
            }
        }
        getFieldData();
    }

    public void getFieldData() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                switch (cells[i][j]) {
                    case NONE:
                        cellsResult[i][j] = ".";
                        break;
                    case X:
                        cellsResult[i][j] = "X";
                        break;
                    case O:
                        cellsResult[i][j] = "O";
                        break;
                }
            }
        }

        controller.sendFileData(cellsResult);
    }

    public void shoot(Point point, Type who) {
        if (point.getX() > SIZE || point.getY() > SIZE
                || point.getX() < 0 || point.getY() < 0) {
            return;
        }

        if (!cells[point.getX()][point.getY()].equals(Type.NONE)) {
            controller.setResume("Ячейка уже занята");
            return;
        }

        controller.setResume("Играем!");
        cells[point.getX()][point.getY()] = who;
    }

    public Type whoIsWinner() {
        if (checkWin(Type.X)) {
            return Type.X;
        }
        if (checkWin(Type.O)) {
            return Type.O;
        }
        return Type.NONE;
    }

    private boolean checkWin(Type t) {
        for (int i = 0; i < SIZE; i++) {
            if (cells[0][i] == t && cells[1][i] == t && cells[2][i] == t) {
                return true;
            }

            if (cells[i][0] == t && cells[i][1] == t && cells[i][2] == t) {
                return true;
            }
        }

        if (cells[0][0] == t && cells[1][1] == t && cells[2][2] == t) {
            return true;
        }

        return cells[0][2] == t && cells[1][1] == t && cells[2][0] == t;
    }
}
