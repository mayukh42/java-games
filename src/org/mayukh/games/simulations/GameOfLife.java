package org.mayukh.games.simulations;

import org.mayukh.games.api.Game;
import org.mayukh.games.api.Point;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by mayukh42 on 26/5/16.
 * http://github.com/mayukh42
 */
public class GameOfLife implements Game {

    private int[] currBoard, nextBoard;
    private final int size = 8;
    private Random generator;

    public GameOfLife () {
        currBoard = new int[size*size];
        nextBoard = new int[size*size];
        generator = new Random();
    }

    void showBoard () {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                System.out.printf("%6s", currBoard[i*size + j] == 1 ? "X" : ".");
            System.out.println();
        }
    }

    int getIndex (int row, int col) {
        row = (row + size) % size;
        col = (col + size) % size;
        return row * size + col;
    }

    Point getCoordinates (int index) {
        if (index < 0 || index >= size*size)
            return null;

        return Point.of(index/size, index%size);
    }

    /*
        nw  n   ne
        w   _   e
        sw  s   se
     */

    int[] getNeighbors (int row, int col) {
        int[] neighbors = new int[8];

        neighbors[0] = getIndex(row-1, col-1);
        neighbors[1] = getIndex(row-1, col);
        neighbors[2] = getIndex(row-1, col+1);

        neighbors[3] = getIndex(row, col-1);
        neighbors[4] = getIndex(row, col+1);

        neighbors[5] = getIndex(row+1, col-1);
        neighbors[6] = getIndex(row+1, col);
        neighbors[7] = getIndex(row+1, col+1);

        return neighbors;
    }

    void evaluate () {
        for (int i = 0; i < size*size; i++)
            nextBoard[i] = 0;
        for (int i = 0; i < size*size; i++) {
            Point p = getCoordinates(i);
            int liveNeighbors = Arrays.stream(getNeighbors(p.getX(), p.getY())).map(
                    n -> currBoard[n]
            ).reduce(
                    0,
                    (a, b) -> a + b
            );
            int alive = currBoard[i];

            // Conway's rules
            if (alive == 1 && liveNeighbors < 2)
                alive = 0;
            else if (alive == 1 && (liveNeighbors == 2 || liveNeighbors == 3))
                alive = 1;
            else if (alive == 1 && liveNeighbors > 3)
                alive = 0;
            else if (alive == 0 && liveNeighbors == 3)
                alive = 1;

            nextBoard[i] = alive;
        }
        System.arraycopy(nextBoard, 0, currBoard, 0, size*size);
    }


    @Override
    public void build() {
        // TODO: Use generator
        Point[] points = {
                Point.of(0, 0),
                Point.of(size/2 - 1, size/2 - 1),
                Point.of(size-1, size-1),
                Point.of(0, size/4),
                Point.of(size-1, size/4),
                Point.of(size/4, 0),
                Point.of(size/4, size-1)
        };
        Arrays.stream(points).forEach(
                point -> currBoard[getIndex(point.getX(), point.getY())] = 1
        );
    }

    @Override
    public void draw() {
        System.out.println("Seed state]");
        showBoard();
    }

    @Override
    public void play() {
        int i = 0;
        while (i < 200) {
            evaluate();
            i++;
        }
        System.out.println("After " + i + " rounds]");
        showBoard();
    }
}
