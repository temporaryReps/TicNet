package server.model;

public class Game {
    public static final int FIRST_USER = 1;
    public static final int SECOND_USER = 2;

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

    /**
     * for game with computer
     *
     * @param userShot
     */
    public void doStep(Point userShot) {
        field.shoot(userShot, Field.Type.X);
        resume = field.getReport();
        fieldData = field.getFieldData();

        if (checkHumanWinner(Field.Type.X)) {
            return;
        }

        field.shoot(computer.getShootPoint(), Field.Type.O);
        if (field.whoIsWinner() == Field.Type.O) {
            resume = "Победил " + Field.Type.O;
        }
    }

    /**
     * for game with another user
     *
     * @param userShot
     * @param userNumber
     */
    public synchronized void doStep(Point userShot, int userNumber) {
        if (userNumber == FIRST_USER) {
            field.shoot(userShot, Field.Type.X);
        } else if (userNumber == SECOND_USER) {
            field.shoot(userShot, Field.Type.O);
        }

        resume = field.getReport();
        fieldData = field.getFieldData();

        if (checkHumanWinner(Field.Type.X)) {
            return;
        }

        checkHumanWinner(Field.Type.O);
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    /**
     * check who is winner among users
     *
     * @param type X or O
     * @return true if type is winner
     */
    private boolean checkHumanWinner(Field.Type type) {
        if (field.whoIsWinner() == type) {
            resume = "Победил " + type;
            return true;
        }

        return false;
    }

    /**
     * initialize field before game is started
     *
     * @return data to class which responsible for connection with user
     */
    public DataContainer initField() {
        return new DataContainer(field.init(), resume);
    }

    /**
     * get field after game's step was done
     *
     * @return data to class which responsible for connection with user
     */
    public DataContainer getFieldData() {
        return new DataContainer(fieldData, resume);
    }

}