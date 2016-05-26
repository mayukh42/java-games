package org.mayukh.games.api;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mayukh42 on 5/23/2016.
 */
public class Card implements Comparable<Card> {

    public enum Suit {
        SPADES, CLUBS, HEARTS, DIAMONDS
    }

    public enum Number {
        ACE (1), TWO (2), THREE (3), FOUR (4), FIVE (5), SIX (6), SEVEN (7),
        EIGHT (8), NINE (9), TEN (10), JACK (11), QUEEN (12), KING (13);

        private final int value;

        Number (int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public String toString () {
            return this.name() + "(" + this.getValue() + ")";
        }
    }

    private final Suit suit;
    private final Number number;

    public Suit getSuit() {
        return suit;
    }

    public Number getNumber() {
        return number;
    }

    public Card (Suit suit, Number number) {
        this.suit = suit;
        this.number = number;
    }

    public static List<Card> getPack (Suit... suits) {
        return Arrays.stream(suits).flatMap(
                s -> Arrays.stream(Number.values()).map(
                        n -> new Card(s, n)
                )
        ).collect(Collectors.toList());
    }

    public Card nextCard () {
        Number[] cardsInSuite = Number.values();
        return new Card(this.suit, cardsInSuite[(this.getNumber().getValue()) % 13]);   // -1 + 1
    }

    public Card prevCard () {
        Number[] cardsInSuite = Number.values();
        return new Card(this.suit, cardsInSuite[(this.getNumber().getValue() + 11) % 13]); // -2 + 13
    }

    public String toString () {
        return this.number.name() + "/" + this.suit.name().substring(0, 1);
    }

    @Override
    public int compareTo(Card o) {
        if (this.getSuit().equals(o.getSuit()))
            return Integer.compare(this.getNumber().getValue(), o.getNumber().getValue());
        else
            return this.getSuit().compareTo(o.getSuit());
    }
}
