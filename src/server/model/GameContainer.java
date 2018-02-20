package server.model;

/**
 * Container for the instance of Game for two users
 */
public class GameContainer {
    private static Game game;

    public static Game getGameInstance() {
        if (game == null) {
            game = new Game();
        }

        return game;
    }
}
