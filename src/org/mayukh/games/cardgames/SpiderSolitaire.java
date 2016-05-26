package org.mayukh.games.cardgames;

import org.mayukh.games.api.Game;
import org.mayukh.games.api.Card;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by mayukh42 on 5/23/2016.
 */

/* Spider Solitaire
 Board Layout
 # # # # # # # # # #
 # # # # # # # # # #
 # # # # # # # # # #
 # # # # # # # # # #
 # # # # V V V V V V
 V V V V
 6x4        5x6

 Hidden: 10x5
 Total: 6x4 + 5x6 + 10x5 = 104 = 13x4x2
 */
public class SpiderSolitaire implements Game {

    private Random generator;
    private List<List<Card>> deck;
    private List<Card> pack;
    private int packIndex;
    private final int cols = 10;
    private int[] rows;
    private int[] from;

    public SpiderSolitaire () {
        this.generator = new Random(100);
        this.deck = new ArrayList<>(cols);
        for (int i = 0; i < 10; i++)
            deck.add(new ArrayList<>());

        this.pack = Card.getPack(Card.Suit.DIAMONDS, Card.Suit.SPADES, Card.Suit.DIAMONDS, Card.Suit.SPADES,
                Card.Suit.DIAMONDS, Card.Suit.SPADES, Card.Suit.DIAMONDS, Card.Suit.SPADES);
        Collections.shuffle(this.pack, this.generator);

        this.packIndex = 0;
        this.rows = new int[] {6, 6, 6, 6, 5, 5, 5, 5, 5, 5};
        this.from = new int[] {6, 6, 6, 6, 5, 5, 5, 5, 5, 5};
    }

    void populateDeck () {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < rows[i]; j++)
                deck.get(i).add(pack.get(packIndex++));
        }
        for (int i = 4; i < cols; i++) {
            for (int j = 0; j < rows[i]; j++)
                deck.get(i).add(pack.get(packIndex++));
        }
    }

    int moves (int column) {
        Card card = deck.get(column).get(from[column]);
        if (card.getNumber().equals(Card.Number.KING))
            return -1;

        int[] res = new int[cols];
        return 0;
    }

    @Override
    public void build() {
        populateDeck();
    }

    @Override
    public void draw() {
        int[] rowIndexes = IntStream.range(0, 10).map(
                n -> 0
        ).toArray();
        int row = 0;
        int maxRow = Arrays.stream(rows).boxed().max(Integer::max).get();
        while (row < maxRow) {
            for (int i = 0; i < cols; i++) {
                if (rowIndexes[i] < rows[i])
                    System.out.printf("%16s", deck.get(i).get(rowIndexes[i]++));
            }
            System.out.println();
            row++;
        }
    }

    @Override
    public void play() {

    }
}
