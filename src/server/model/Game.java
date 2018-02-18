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

    public void doStep(Point userShot) {
        field.shoot(userShot, Field.Type.X);
        resume = field.getReport();
        fieldData = field.getFieldData();

        if (field.whoIsWinner() == Field.Type.X) {
            resume = "Победил " + Field.Type.X;
            return;
        }

        field.shoot(computer.getShootPoint(), Field.Type.O);
        if (field.whoIsWinner() == Field.Type.O) {
            resume = "Победил " + Field.Type.O;
        }
    }

    /**
     * initialize field before game is started
     * @return data to class which responsible for connection with user
     */
    public DataContainer initField() {
        return new DataContainer(field.init(), resume);
    }

    /**
     * get field after game's step was done
     * @return data to class which responsible for connection with user
     */
    public DataContainer getFieldData() {
        return new DataContainer(fieldData, resume);
    }
}