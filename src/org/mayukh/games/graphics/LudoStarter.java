package org.mayukh.games.graphics;

import processing.core.PApplet;

/**
 * Created by mayukh42 on 5/18/2016.
 */
public class LudoStarter extends PApplet {

    enum Position {
        SW ("096669996D9E01A24BD"),
        NW ("0066666916161112424"),
        NE ("9066669681160A1BD24"),
        SE ("99669699D89E1AABDBD");

        private final String coordinates;

        Position (String coordinates) {
            this.coordinates = coordinates;
        }

        int[] parse () {
            return coordinates.chars().map(
                    c -> c >= 'A' && c <= 'F' ? 10 + c - 'A' : c - 48
            ).toArray();
        }
    }

    enum LudoColor {
        RED (203, 58, 58, Position.SW),
        GREEN (68, 203, 58, Position.NW),
        BLUE (58, 82, 203, Position.NE),
        YELLOW (224, 222, 52, Position.SE);

        private final int red;
        private final int green;
        private final int blue;
        private final Position position;

        LudoColor (int red, int green, int blue, Position position) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.position = position;
        }
    }

    private final int boxSize = 50;
    private final int boardSize = 15;

    public void settings () {
        size(boxSize * (boardSize + boardSize/5), boxSize * boardSize);
    }

    public void setup () {
        drawLudoBoard();
    }

    void drawLudoBoard () {
        fill(255);
        rect(0, 0, boxSize * boardSize, boxSize * boardSize);

        drawColorBox(LudoColor.RED);
        drawColorBox(LudoColor.GREEN);
        drawColorBox(LudoColor.BLUE);
        drawColorBox(LudoColor.YELLOW);

        drawOthers();
    }

    void drawPucks (int x1, int x2, int y1, int y2) {
        ellipse(boxSize * x1, boxSize * y1, boxSize, boxSize);
        ellipse(boxSize * x2, boxSize * y1, boxSize, boxSize);
        ellipse(boxSize * x1, boxSize * y2, boxSize, boxSize);
        ellipse(boxSize * x2, boxSize * y2, boxSize, boxSize);
    }

    void drawColorBox (LudoColor color) {
        fill(color.red, color.green, color.blue);
        int[] code = color.position.parse();

        rect(boxSize * code[0], boxSize * code[1], boxSize * code[2], boxSize * code[3]);
        triangle(boxSize * code[4], boxSize * code[5], boxSize * code[6], boxSize * code[7], boxSize * 7.5f, boxSize * 7.5f);

        rect(boxSize * code[8], boxSize * code[9], boxSize, boxSize);
        for (int n = code[10]; n < code[11]; n++) {
            if (code[12] == 0)
                rect(boxSize * 7, boxSize * n, boxSize, boxSize);
            else
                rect(boxSize * n, boxSize * 7, boxSize, boxSize);
        }

        fill(255);
        rect(boxSize * code[13], boxSize * code[14], boxSize * 4, boxSize * 4);

        fill(color.red, color.green, color.blue);
        drawPucks(code[15], code[16], code[17], code[18]);
    }

    void drawOthers () {
        fill(255);

        for (int n = 6; n < 9; n++) {
            rect(boxSize * n, 0, boxSize, boxSize);
            rect(boxSize * n, boxSize * 14, boxSize, boxSize);
            rect(0, boxSize * n, boxSize, boxSize);
            rect(boxSize * 14, boxSize * n, boxSize, boxSize);
        }

        for (int n = 2; n < 13; n++) {
            if (!(n >= 6 && n < 9)) {
                rect(boxSize * 6, boxSize * n, boxSize, boxSize);
                rect(boxSize * 8, boxSize * n, boxSize, boxSize);
                rect(boxSize * n, boxSize * 6, boxSize, boxSize);
                rect(boxSize * n, boxSize * 8, boxSize, boxSize);
            }
        }
    }
}
