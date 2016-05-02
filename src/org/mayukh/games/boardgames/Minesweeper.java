package org.mayukh.games.boardgames;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mayukh42 on 27/4/16.
 * http://github.com/mayukh42
 */
public class Minesweeper implements Game {

    private int[] board;
    private int x;
    private int y;
    private int numMines;
    private Random generator;

    public Minesweeper (int x, int y) {
        this.x = x;
        this.y = y;
        this.board = new int[this.x * this.y];
        this.numMines = x + 1;
        this.generator = new Random();
    }

    private List<Integer> getNeighbors (int n) {
        if (n < 0 || n >= this.x * this.y)
            return null;

        List<Integer> list = new ArrayList<>();
        list = getNorth(n, list);
        list = getWest(n, list);
        list = getEast(n, list);
        list = getSouth(n, list);
        return list;
    }

    private List<Integer> getNorth (int n, List<Integer> list) {
        if (n < this.x) {
            // north face
            return list;
        }
        for (int i = n - (this.x + 1); i <= n - (this.x - 1); i++) {
            if (i >= 0 && n/this.x == 1 + i/this.x)
                list.add(i);
        }
        return list;
    }

    private List<Integer> getEast (int n, List<Integer> list) {
        if (n % this.x == this.x - 1) {
            // east face
            return list;
        }
        int i = n + 1;
        if (i/this.x == n/this.x)
            list.add(i);
        return list;
    }

    private List<Integer> getSouth (int n, List<Integer> list) {
        if (n >= this.x * (this.y - 1) && n < this.x * this.y) {
            // south face
            return list;
        }
        for (int i = n + (this.x - 1); i <= n + (this.x + 1); i++) {
            if (i < this.x * this.y && i/this.x == 1 + n/this.x)
                list.add(i);
        }
        return list;
    }

    private List<Integer> getWest (int n, List<Integer> list) {
        if (n % this.x == 0) {
            // west face
            return list;
        }
        int i = n - 1;
        if (i/this.x == n/this.x)
            list.add(i);
        return list;
    }

    private void printBoard (boolean mines) {
        for (int i = 0; i < this.y; i++) {
            for (int j = 0; j < this.x; j++)
                System.out.format("%6d", mines ? board[i * this.x + j] : i * this.x + j);
            System.out.println();
        }
    }

    private int countMinesAround (int n) {
        List<Integer> neighbors = getNeighbors(n);
        return neighbors == null ? -1 : neighbors.stream().map(
                a -> board[a]
        ).reduce(
                0,
                (a, b) -> a + b
        );
    }

    @Override
    public void build() {
        int i = 0;
        while (i < numMines) {
            int position = generator.nextInt(this.x * this.y);
            if (board[position] == 0) {
                board[position] = 1;
                i++;
            }
        }
    }

    @Override
    public void draw() {
        printBoard(false);
    }

    @Override
    public void play() {
        for (int i = 0; i < this.x * this.y; i++)
            System.out.println("Number of mines around " + i + " = " + countMinesAround(i));
    }
}
