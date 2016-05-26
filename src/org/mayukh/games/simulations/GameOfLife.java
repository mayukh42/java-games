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

    private int[] board;
    private final int size = 16;
    private Random generator;

    public GameOfLife () {
        board = new int[size*size];
        generator = new Random();
    }

    int getIndex (int row, int col) {
        row = (row + size) % size;
        col = (col + size) % size;

        System.out.println(row + ", " + col);
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

    public int[] getNeighbors (int row, int col) {
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


    @Override
    public void build() {
        Point[] points = {
                Point.of(0, 0),
                Point.of(7, 7),
                Point.of(15, 15),
                Point.of(0, 4),
                Point.of(15, 4),
                Point.of(4, 0),
                Point.of(4, 15)
        };
        Arrays.stream(points).forEach(
                point -> System.out.println(Arrays.toString(getNeighbors(point.getX(), point.getY())))
        );
    }

    @Override
    public void draw() {

    }

    @Override
    public void play() {

    }
}
