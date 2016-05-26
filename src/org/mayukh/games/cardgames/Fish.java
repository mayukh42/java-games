package org.mayukh.games.cardgames;

import org.mayukh.games.api.Card;
import org.mayukh.games.api.Game;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mayukh42 on 5/25/2016.
 *
 * A Fish variant for up to 4 players with 8 cards a hand
 */
public class Fish implements Game {

    private List<Set<Card>> hands;
    private Queue<Card> deck;
    private Map<Card, Integer> currentHandScore;
    private final int handSize = 8;
    private Random generator;

    public Fish() {
        generator = new Random();
        List<Card> cards = Card.getPack(Card.Suit.SPADES, Card.Suit.CLUBS, Card.Suit.HEARTS, Card.Suit.DIAMONDS);
        Collections.shuffle(cards, generator);
        deck = new LinkedList<>(cards);

        currentHandScore = new TreeMap<>();
    }

    void generateHands (int numPlayers) {
        if (numPlayers < 2 || numPlayers > 4)
            return;

        hands = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++)
            hands.add(new TreeSet<>());

        for (int i = 0; i < handSize; i++) {
            hands.stream().forEach(
                    h -> h.add(deck.poll())
            );
        }
    }

    void showHand (int playerIndex) {
        if (playerIndex < 0 || playerIndex >= hands.size())
            return;

        System.out.print("Hand [" + playerIndex + "]: ");
        System.out.println(hands.get(playerIndex));
    }

    boolean dropCards (int playerIndex, Set<Card> hand) {
        boolean skipTurn = false;
        Set<Card> hand2 = new TreeSet<>(hand);
        for (Card card : hand2) {
            // 5-way
            Card prev = card.prevCard();
            Card prevPrev = prev.prevCard();
            Card next = card.nextCard();
            Card nextNext = next.nextCard();
            if (hand.contains(prev) && hand.contains(prevPrev) && hand.contains(next) && hand.contains(nextNext)) {
                hand.remove(prev);
                hand.remove(prevPrev);
                hand.remove(card);
                hand.remove(next);
                hand.remove(nextNext);
                skipTurn = true;
                break;
            }
            // 3-way
            else if (hand.contains(prev) && hand.contains(next)) {
                hand.remove(prev);
                hand.remove(card);
                hand.remove(next);
                skipTurn = true;
                break;
            }
            // 3-way or 4-way, same number
            List<Card> sameNumber = hand.stream().filter(
                    d -> d.getNumber().getValue() == card.getNumber().getValue() && !d.equals(card)
            ).collect(Collectors.toList());
            if (sameNumber.size() >= 3) {
                for (Card c : sameNumber)
                    hand.remove(c);
                skipTurn = true;
            }
        }
        if (skipTurn)
            System.out.println("Hand [" + playerIndex + "] after drop: " + hand);

        return skipTurn;
    }

    void turn (int playerIndex, Card card) {
        currentHandScore.clear();
        Set<Card> hand = hands.get(playerIndex);
        System.out.println("Hand [" + playerIndex + "]: " + hand);
        System.out.println("Hand [" + playerIndex + "]: New card: " + card);

        if (dropCards(playerIndex, hand))
            return;

        hand.add(card);
        hand.stream().forEach(
                c -> currentHandScore.put(c, 0)
        );

        hand.stream().forEach(
                c -> {
                    int score = 0;
                    if (hand.contains(c.nextCard()) || hand.contains(c.prevCard()))
                        score += 100;

                    List<Card> sameNumber = hand.stream().filter(
                            d -> d.getNumber().getValue() == c.getNumber().getValue() && !d.equals(c)
                    ).collect(Collectors.toList());

                    List<Card> sameSuite = hand.stream().filter(
                            d -> d.getSuit().equals(c.getSuit()) && !d.equals(c)
                    ).collect(Collectors.toList());

                    score += 100 * sameNumber.size() + 50 * sameSuite.size();
                    currentHandScore.put(c, score);
                }
        );
        System.out.println("Hand [" + playerIndex + "]: " + currentHandScore);

        List<Map.Entry<Card, Integer>> scores = new ArrayList<>(currentHandScore.entrySet());
        Collections.sort(scores, Map.Entry.comparingByValue());
        hand.remove(scores.get(0).getKey());
        System.out.println("Hand [" + playerIndex + "]: " + hand);
    }

    boolean isGameOver (int playerIndex) {
        Set<Card> hand = hands.get(playerIndex);
        if (hand.isEmpty()) {
            System.out.println(playerIndex + " wins!");
            return true;
        }
        else if (hand.size() < 3) {
            System.out.println(playerIndex + " loses!");
            return true;
        }
        return false;
    }

    @Override
    public void build() {
        generateHands(2);
    }

    @Override
    public void draw() {
        for (int i = 0; i < 2; i++)
            showHand(i);
    }

    @Override
    public void play() {
        boolean gameOver = false;
        while (!gameOver) {
            for (int i = 0; i < 2; i++) {
                gameOver = isGameOver(i);
                if (gameOver)
                    break;
                else {
                    Card card = deck.poll();
                    if (card != null)
                        turn(i, card);
                    else {
                        gameOver = true;
                        System.out.println("Game drawn!");
                    }
                }
            }
        }
    }
}
