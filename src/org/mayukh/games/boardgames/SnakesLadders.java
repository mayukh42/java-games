package org.mayukh.games.boardgames;

import java.util.Random;

/**
 * Created by mamukhop on 4/26/2016.
 */
public class SnakesLadders implements Game {

    private int[] board;
    private Random generator;

    public SnakesLadders () {
        this.board = new int[100];
        this.generator = new Random();
    }

    @Override
    public void build() {
        int numSnakes = 8 + generator.nextInt(12);
        int numLadders = 10 + generator.nextInt(10);

        System.out.println("This board has " + numSnakes + " snakes and " + numLadders + " ladders.");

        int i = 0;
        while (i < numLadders) {
            int position = generator.nextInt(88) + 2;   // ladders should only be in range [2, 89]
            if (board[position - 1] != 0)
                continue;

            board[position - 1] = generator.nextInt(99 - position);
            i++;
        }

        i = 0;
        while (i < numSnakes) {
            int position = generator.nextInt(87) + 11;   // snakes should only be in range [11, 97]
            if (board[position - 1] != 0)
                continue;

            board[position - 1] = -1 * generator.nextInt(position - 1);
            i++;
        }
    }

    @Override
    public void draw() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++)
                System.out.format("%6d", board[i*10 + j]);
            System.out.println();
        }
    }

    @Override
    public void play() {
        Player alice = new Player(0, "Alice");
        Player bob = new Player(0, "Bob");
        System.out.println(alice.getName() + " and " + bob.getName() + " are playing.");

        boolean gameOver = false;
        while (!gameOver) {
            gameOver = turn(alice);
            if (gameOver)
                break;

            gameOver = turn(bob);
        }
    }

    private boolean turn (Player player) {
        int dice = generator.nextInt(6) + 1;
        String name = player.getName();
        System.out.print(name + " throws " + dice);

        int score = player.getScore();
        if (score + dice > 100) {
            System.out.println(" , No turn for " + name + ". ");
            return false;
        }

        score = player.setScoreAndGet(score + dice);
        if (score == 100) {
            System.out.println(" [" + name + " wins!]");
            return true;
        }

        while (board[score - 1] != 0) {
            if (board[score - 1] < 0)
                System.out.print(", " + name + " loses " + (-1 * board[score - 1]) + " points");
            else
                System.out.print(", " + name + " gains " + board[score - 1] + " points");
            score = player.setScoreAndGet(score + board[score - 1]);
        }
        System.out.println(", " + name + " is at " + score);
        return false;
    }
}
