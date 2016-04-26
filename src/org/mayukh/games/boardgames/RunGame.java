package org.mayukh.games.boardgames;

/**
 * Created by mamukhop on 4/26/2016.
 */
public class RunGame {

    public static void main(String[] args) {
        // playSnakesLadders();
        playLudo();
    }

    private static void playSnakesLadders () {
        Game game = new SnakesLadders();
        game.build();
        game.draw();
        game.play();
    }

    private static void playLudo () {
        Game game = new Ludo();
        game.build();
        game.draw();
        game.play();
    }
}
