package org.mayukh.games.cardgames;

import org.mayukh.games.api.Card;
import org.mayukh.games.api.Game;

import java.util.List;

/**
 * Created by mayukh42 on 5/23/2016.
 */
public class RunCardGame {

    public static void main(String[] args) {
        // playCards();
        // playSpiderSolitaire();
        playFish();
    }

    static void playSpiderSolitaire () {
        Game game = new SpiderSolitaire();
        game.build();
        game.draw();
        game.play();
    }

    static void playFish () {
        Game game = new Fish();
        game.build();
        game.draw();
        game.play();
    }

    static void playCards () {
        List<Card> deck = Card.getPack(Card.Suit.SPADES, Card.Suit.HEARTS);
        System.out.println("Size of deck = " + deck.size());
        System.out.println(deck);
    }
}
