package org.mayukh.games.api;

/**
 * Created by mayukh42 on 26/5/16.
 * http://github.com/mayukh42
 */
public class Point {

    private final int x;
    private final int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Point of (int x, int y) {
        return new Point(x, y);
    }

    private Point (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString () {
        return "(" + this.x + ", " + this.y + ")";
    }
}
