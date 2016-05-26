package org.mayukh.games.api;

/**
 * Created by mayukh42 on 4/26/2016.
 * https://github.com/mayukh42/java-games
 */
public class Player {

    private int score;
    private final String name;

    public Player (int score, String name) {
        this.score = score;
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int setScoreAndGet(int score) {
        setScore(score);
        return getScore();
    }

    public String getName() {
        return name;
    }
}
