package server.model;

public class Game {
    private Field field;
    private Computer computer;
    private String resume;
    private String[][] fieldData;

    public Game() {
        field = new Field();
        computer = new Computer();
        resume = "Играем";

        field.init();
    }

    public String getResume() {
        return resume;
    }

    public void doStep(Point userShot) {
        field.shoot(userShot, Field.Type.X);
        resume = field.getReport();
        if (field.whoIsWinner() == Field.Type.X) {
            resume = "Победил " + Field.Type.X;
            fieldData = field.getFieldData();
            return;
        }

        field.shoot(computer.getShootPoint(), Field.Type.O);
        if (field.whoIsWinner() == Field.Type.O) {
            resume = "Победил " + Field.Type.O;
        }
        fieldData = field.getFieldData();
    }


    public String[][] initField() {
        return field.init();
    }

    public String[][] getFieldData() {
        return fieldData;
    }
}