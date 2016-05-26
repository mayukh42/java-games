package org.mayukh.games.simulations;

import org.mayukh.games.api.Game;

/**
 * Created by mayukh42 on 26/5/16.
 * http://github.com/mayukh42
 */
public class RunSimulation {

    public static void main(String[] args) {
        runGameOfLife();
    }

    static void runGameOfLife () {
        Game game = new GameOfLife();
        game.build();
        game.draw();
        game.play();
    }
}
