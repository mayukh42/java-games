package org.mayukh.games.boardgames;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by mayukh42 on 4/26/2016.
 * https://github.com/mayukh42/java-games
 *
 * Implementation of the Ludo board game (https://en.wikipedia.org/wiki/Ludo_(board_game))
 * 2, 3, 4 players can play - game stops when any one player wins
 * If a puck meets opponent's puck while in the wild (i.e. not in safe zone), then the opponent's puck is sent home
 *
 * Ref. data/ludo.txt for schematic diagram of the board.
 */
public class Ludo implements Game {

    enum Zone {
        HOME, SAFE, WILD, CENTER
    }

    class Puck {
        int position;
        int start;
        int target;
        int diversion;
        Color color;
        Zone zone;

        Puck (Color color) {
            int position = -1, target = 57;
            switch (color) {
                case GREEN:
                    setAttributes(position + 13, target + 5);
                    break;
                case BLUE:
                    setAttributes(position + 26, target + 10);
                    break;
                case YELLOW:
                    setAttributes(position + 39, target + 15);
                    break;
                default:
                    setAttributes(position, target);
            }
            this.color = color;
            this.zone = Zone.HOME;
        }

        private void setAttributes (int position, int target) {
            this.position = position;
            this.start = position + 1;
            this.target = target;
            this.diversion = position - 1 < 0 ? 50 : position - 1;
        }

        void sendHome () {
            this.position = start - 1;
            this.zone = Zone.HOME;
        }

        public String toString () {
            return this.color.name() + " [" + this.position + "]";
        }
    }

    private int[] board;
    private Puck[] pucks;
    private Random generator;

    public Ludo () {
        this.board = new int[72];
        this.pucks = new Puck[16];
        this.generator = new Random();
    }

    private int nextCell (Puck puck) {
        int cell = puck.position;
        if (cell == puck.diversion) {
            puck.zone = Zone.SAFE;
            return board[cell];
        }
        else if (cell == puck.target - 1)
            return puck.target;
        else if (cell == puck.start) {
            puck.zone = Zone.WILD;
            return cell + 1;
        }
        else if (puck.zone == Zone.SAFE)
            return cell + 1;
        else
            return cell < 51 ? cell + 1 : 0;
    }

    private int getColorCode (Color color) {
        int code;
        switch (color) {
            case GREEN:
                code = 1;
                break;
            case BLUE:
                code = 2;
                break;
            case YELLOW:
                code = 3;
                break;
            default:
                code = 0;
        }
        return code;
    }

    private Player addPlayer (Color color) {
        int low = getColorCode(color) * 4;
        Player player;

        for (int i = low; i < low + 4; i++) {
            if (pucks[i] != null) {
                System.out.println(" Player already exists! ");
                return null;
            }
            pucks[i] = new Puck(color);
        }
        player = new Player(-1, color.name());
        return player;
    }

    private Puck getPuckAtCurrent (Puck puck) {
        Puck curr = null;
        List<Puck> opponents = Arrays.asList(this.pucks).stream().filter(
                p -> p.zone == Zone.WILD && p.color != puck.color && p.position == puck.position
        ).collect(Collectors.toList());

        if (opponents.size() > 0)
            curr = opponents.get(0);

        return curr;
    }

    Player[] setPlayers (Color... colors) {
        if (colors.length < 1 || colors.length > 4)
            return null;

        Player[] players = new Player[colors.length];
        for (int i = 0; i < colors.length; i++)
            players[i] = addPlayer(colors[i]);

        return players;
    }

    boolean turn (Player player) {
        int dice = generator.nextInt(6) + 1;
        System.out.print(player.getName() + " throws " + dice + ", ");

        List<Puck> pucksHWSC = Arrays.asList(this.pucks);

        List<Puck> pucksH = pucksHWSC.stream().filter(
                p -> (p != null) && player.getName().equalsIgnoreCase(p.color.name()) && p.zone == Zone.HOME
        ).collect(Collectors.toList());

        List<Puck> pucksWS = pucksHWSC.stream().filter(
                p -> (p != null) && player.getName().equalsIgnoreCase(p.color.name()) && (p.zone == Zone.WILD || p.zone == Zone.SAFE)
        ).collect(Collectors.toList());

        Puck curr = null;
        if ((dice == 6 && pucksH.size() > 0) || pucksH.size() == 4) {
            curr = pucksH.get(0);
        }

        if (curr == null && pucksWS.size() > 0) {
            curr = pucksWS.get(0);
            int pos = curr.position;
            for (Puck puck : pucksWS) {
                if (pos > puck.position) {
                    pos = puck.position;
                    curr = puck;
                }
            }
        }
        return curr == null || turn (curr, player, dice);
    }

    boolean isWinner (Player player) {
        List<Puck> pucks = Arrays.asList(this.pucks).stream().filter(
                p -> (p != null) && player.getName().equalsIgnoreCase(p.color.name())
        ).collect(Collectors.toList());

        boolean won = true;
        for (Puck puck : pucks) {
            won = puck.position == puck.target;
            if (!won)
                break;
        }
        return won;
    }

    void showPlayerPosition (Player player) {
        List<Puck> pucks = Arrays.asList(this.pucks).stream().filter(
                p -> (p != null) && player.getName().equalsIgnoreCase(p.color.name())
        ).collect(Collectors.toList());

        for (Puck puck : pucks)
            System.out.print(puck + " ");
        System.out.println();
    }

    boolean turn (Puck puck, Player player, int dice) {
        if (puck.zone == Zone.HOME && dice == 6) {
            puck.zone = Zone.SAFE;
            puck.position = puck.start;
            showPlayerPosition(player);
            return false;
        }

        if (puck.zone != Zone.HOME && dice + puck.position <= puck.target) {
            while (dice > 0) {
                puck.position = nextCell(puck);
                if (isWinner(player)) {
                    puck.zone = Zone.CENTER;
                    showPlayerPosition(player);
                    return true;
                }
                dice--;
            }
            Puck other = getPuckAtCurrent(puck);
            if (other != null) {
                System.out.print(" \n\tOpponent found! (" + other + ") is sent home. ");
                other.sendHome();
            }
        }
        else
            System.out.print("No turn. ");

        showPlayerPosition(player);
        return false;
    }


    @Override
    public void build() {
        board[50] = 52; board[11] = 57; board[24] = 62; board[37] = 67;
    }

    @Override
    public void draw() {

    }

    @Override
    public void play() {
        Player[] players = setPlayers(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);

        while (true) {
            boolean gameOver = false;
            for (Player player : players) {
                if (turn(player)) {
                    System.out.println(player.getName() + " wins!");
                    gameOver = true;
                    break;
                }
            }
            if (gameOver)
                break;
        }
    }
}
