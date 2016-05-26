package org.mayukh.games.boardgames;

import org.mayukh.games.api.Game;

import java.util.Arrays;
import java.util.Random;;
import java.util.stream.Collectors;

/**
 * Created by mayukh42 on 30/4/16.
 * http://github.com/mayukh42
 */
public class TicTacToe implements Game {

    enum Symbol {
        X, O;

        Symbol toggle () {
            return this == Symbol.X ? Symbol.O : Symbol.X;
        }
    }

    private boolean ai;
    private Symbol[] board;
    private final int SIZE = 3;
    private int[] choiceIdx;
    private Random generator;

    public TicTacToe () {
        this.ai = true;
        this.board = new Symbol[SIZE * SIZE];
        this.choiceIdx = new int[SIZE * SIZE];
        for (int i = 0; i < SIZE * SIZE; i++)
            this.choiceIdx[i] = -1;
        this.generator = new Random();
    }

    public Symbol[] copyBoard (Symbol[] src) {
        Symbol[] newBoard = new Symbol[SIZE * SIZE];
        System.arraycopy(src, 0, newBoard, 0, SIZE * SIZE);
        return newBoard;
    }

    // when AI is turned off
    private int getSlot (Symbol[] game) {
        int i = -1;
        while (i < 0) {
            int j = generator.nextInt(9);
            if (game[j] == null)
                i = j;
        }
        return i;
    }

    private int getFilledCount (Symbol[] game) {
        return Arrays.asList(game).stream().filter(
                s -> s != null
        ).collect(Collectors.toList()).size();
    }

    private int[] getEmptySlots (Symbol[] game) {
        int filled = getFilledCount(game);
        int[] emptySlots = new int[SIZE * SIZE - filled];
        int j = 0;
        for (int i = 0; i < SIZE * SIZE; i++) {
            if (game[i] == null)
                emptySlots[j++] = i;
        }
        return emptySlots;
    }

    /*
        0 1 2
        3 4 5
        6 7 8
     */

    boolean isTriplet (Symbol a, int b, int c, Symbol[] game) {
        return a == game[b] && a == game[c];
    }

    void printBoard (Symbol[] game, int n, Symbol value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8*n; i++)
            sb.append(" ");
        System.out.println(sb.toString() + "Game state [" + value.name() + "] ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(sb.toString());
            for (int j = 0; j < SIZE; j++)
                System.out.format("%2s", game[i*SIZE + j] != null ? game[i * SIZE + j].name() : "_");
            System.out.println();
        }
    }

    int selectScore (int[] scores, int[] indexes, Symbol value, int level) {
        int score = 0;
        for (int i : indexes) {
            if (value == Symbol.X) {
                if (scores[i] > score) {
                    score = scores[i];
                    this.choiceIdx[level] = i;
                }
            }
            else {
                if (scores[i] < score) {
                    score = scores[i];
                    this.choiceIdx[level] = i;
                }
            }
        }
        return score;
    }

    int miniMaxAlgorithm (Symbol value, int level) {
        if (level < 3) {
            int index = generator.nextInt(SIZE * SIZE);
            while (board[index] != null)
                index = generator.nextInt(SIZE * SIZE);

            this.choiceIdx[level] = index;
            return 0;
        }

        int[] emptySlots = getEmptySlots(board);
        int[] scores = new int[SIZE * SIZE];
        for (int slot : emptySlots) {
            board[slot] = value;
            if (isWinningMove(slot, value, board))
                scores[slot] = value == Symbol.X ? 10 : -10;
            else
                scores[slot] = miniMaxAlgorithm(value.toggle(), level + 1);
            board[slot] = null;
        }
        System.out.println("[Level " + level + "] Scores: " + Arrays.toString(scores));
        int score = selectScore(scores, emptySlots, value, level);
        if (level < 9 && choiceIdx[level] == -1)
            choiceIdx[level] = emptySlots[generator.nextInt(emptySlots.length)];
        return score;
    }

    boolean isWinningMove (int index, Symbol value, Symbol[] game) {
        boolean res;
        switch (index) {
            case 0:
                res = isTriplet(value, 1, 2, game) || isTriplet(value, 3, 6, game) || isTriplet(value, 4, 8, game);
                break;
            case 1:
                res = isTriplet(value, 0, 2, game) || isTriplet(value, 4, 7, game);
                break;
            case 2:
                res = isTriplet(value, 0, 1, game) || isTriplet(value, 4, 6, game) || isTriplet(value, 5, 8, game);
                break;
            case 3:
                res = isTriplet(value, 0, 6, game) || isTriplet(value, 4, 5, game);
                break;
            case 5:
                res = isTriplet(value, 2, 8, game) || isTriplet(value, 3, 4, game);
                break;
            case 6:
                res = isTriplet(value, 0, 3, game) || isTriplet(value, 2, 4, game) || isTriplet(value, 7, 8, game);
                break;
            case 7:
                res = isTriplet(value, 6, 8, game) || isTriplet(value, 1, 4, game);
                break;
            case 8:
                res = isTriplet(value, 0, 4, game) || isTriplet(value, 2, 5, game) || isTriplet(value, 6, 7, game);
                break;
            default:    // case 4 (center)
                res = isTriplet(value, 1, 7, game) || isTriplet(value, 3, 5, game) || isTriplet(value, 0, 8, game) || isTriplet(value, 2, 6, game);
        }
        return res;
    }

    // test method
    void fill (Symbol s, int... indexes) {
        for (int index : indexes)
            board[index] = s;
    }

    @Override
    public void build() {
        /*
            _ X O
            _ X _
            X O O
         */
        fill(Symbol.X, 6, 1, 4);
        fill(Symbol.O, 8, 7, 2);
    }

    @Override
    public void draw() {
        printBoard(board, 0, Symbol.X);
    }

    @Override
    public void play() {
        Symbol value;
        int scoreIdx, level = 0;

        value = Symbol.X;
        // value = Symbol.O;
        level = 6;
        scoreIdx = miniMaxAlgorithm(value, level);
        System.out.println("Minimax result for " + value.name() + ": " + scoreIdx + " @ " + Arrays.toString(this.choiceIdx) + ", index = " + this.choiceIdx[level]);
    }
}
