package org.mayukh.games.boardgames;

import org.mayukh.games.api.Game;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mayukh42 on 5/4/2016.
 * https://github.com/mayukh42/java-games
 */
public class Sudoku implements Game {

    private Map<String, String> board;
    Set<String> rawUnits;
    private Map<String, List<String>> units;
    private Map<String, Set<String>> peers;
    private final String[] rows = {"ABC", "DEF", "GHI"};
    private final String[] cols = {"123", "456", "789"};

    public Sudoku () {
        board = new TreeMap<>();
        rawUnits = new TreeSet<>();
        units = new TreeMap<>();
        peers = new TreeMap<>();     // 9 rows, 9 cols, 9 boxes
    }

    private String[] generate (String first, String second) {
        int length = first.length() * second.length();
        String[] res = new String[length];
        int i = 0;
        for (char f : first.toCharArray()) {
            for (char s : second.toCharArray()) {
                char[] fs = {f, s};
                res[i++] = new String(fs);
            }
        }
        return res;
    }

    private void createKeysUnitsPeers () {
        String alphabets = String.join("", rows);
        String numbers = String.join("", cols);

        // board
        String[] keys = generate(alphabets, numbers);
        Arrays.asList(keys).stream().forEach(
                k -> board.put(k, numbers)
        );

        // raw units
        for (String rowSet: rows) {
            for (String colSet: cols)
                rawUnits.add(rowSet + colSet);
        }
        Character[] alphanums = (alphabets + numbers).chars().mapToObj(
                c -> (char)c
        ).toArray(Character[]::new);
        Arrays.stream(alphanums).forEach(
                c -> rawUnits.add(c.toString())  // c is Character
        );

        // units
        board.keySet().stream().forEach(
                k -> {
                    rawUnits.stream().filter(
                            u -> (u.length() == 1 && (u.contains(k.substring(0, 1)) || u.contains(k.substring(1, 2)))) ||
                                    u.contains(k.substring(0, 1)) && u.contains(k.substring(1, 2))
                    ).forEach(
                            uf -> {
                                units.putIfAbsent(k, new ArrayList<>());
                                units.get(k).add(uf);
                            }
                    );
                }
        );

        // peers
        units.keySet().stream().forEach(
                u -> {
                    Set<String> ps = units.get(u).stream().flatMap(
                            unit -> getCellsInUnit(unit).stream()
                    ).collect(Collectors.toSet());
                    ps.remove(u);
                    peers.putIfAbsent(u, ps);
                }
        );
    }

    private void eliminate (String cell, String num) {
        peers.get(cell).stream().forEach(
                p -> {
                    StringBuilder sb = new StringBuilder(board.get(p));
                    int index = sb.indexOf(num);
                    if (index != -1)
                        sb.deleteCharAt(index);
                    board.put(p, sb.toString());
                }
        );
    }

    private List<String> getCellsInUnit (String unit) {
        String alphabets = String.join("", rows);
        String numbers = String.join("", cols);
        List<String> cells = new ArrayList<>();

        if (unit.length() == 1) {
            char g = unit.charAt(0);
            if (g >= 'A' && g <= 'I') {
                for (char c : numbers.toCharArray())
                    cells.add(unit + String.valueOf(c));
            }
            else if (g >= '1' && g <= '9') {
                for (char c : alphabets.toCharArray())
                    cells.add(String.valueOf(c) + unit);
            }
        }
        else {
            String gridAlpha = unit.substring(0, 3);
            String gridNum = unit.substring(3, 6);
            for (char a : gridAlpha.toCharArray()) {
                for (char n : gridNum.toCharArray())
                    cells.add(String.valueOf(a) + String.valueOf(n));
            }
        }
        return cells;
    }

    private void createPuzzle (String grid) {
        if (grid.length() != 81)
            return;

        int i = 0;
        for (String cell : board.keySet()) {
            if (grid.charAt(i) != '.')
                board.put(cell, String.valueOf(grid.charAt(i)));
            i++;
        }
    }

    private void display () {
        int i = 0;
        for (String cell : board.keySet()) {
            if (i != 0 && i % 27 == 0)
                System.out.print("\n---------+---------+---------");
            if (i != 0 && i % 9 == 0)
                System.out.println();
            String value = board.get(cell);
            if (i % 3 == 0 && i % 9 != 0)
                System.out.print("|");
            System.out.format(" %1s ", value.length() > 1 ? "_" : value);
            i++;
        }
        System.out.println("\n");
    }

    private boolean parseGrid (Set<String> solved, PriorityQueue<Map.Entry<String, String>> pq) {
        if (pq.isEmpty())
            return true;

        String cell = pq.poll().getKey();
        Character[] values = board.get(cell).chars().mapToObj(
                c -> (char) c
        ).toArray(Character[]::new);

        return false;
    }

    @Override
    public void build() {
        createKeysUnitsPeers();

        String grid = "4.....8.5.3..........7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......";
        createPuzzle(grid);
    }

    @Override
    public void draw() {
        System.out.println(board);
        System.out.println(rawUnits);
        System.out.println(units);
        System.out.println(peers);
        System.out.println();
        display();
    }

    @Override
    public void play() {
        Set<String> solved = new TreeSet<>(board.keySet().stream().filter(
                cell -> board.get(cell).length() == 1
        ).collect(Collectors.toSet()));
        PriorityQueue<Map.Entry<String, String>> q = new PriorityQueue<>(
                Comparator.comparing((Map.Entry<String, String> element) -> element.getValue().length())
        );
        board.keySet().stream().forEach(
                cell -> {
                    if (solved.contains(cell))
                        eliminate(cell, board.get(cell));
                }
        );
        q.addAll(board.entrySet().stream().filter(
                cell -> !solved.contains(cell.getKey())
        ).collect(Collectors.toSet()));
        System.out.println(q);
        System.out.println(board);
    }
}
