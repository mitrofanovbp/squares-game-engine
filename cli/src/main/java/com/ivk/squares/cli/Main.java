package com.ivk.squares.cli;

import com.ivk.squares.engine.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    enum PlayerType {USER, COMP}

    record Player(PlayerType type, Color color) {
    }

    private static final Pattern GAME = Pattern.compile("^\\s*GAME\\s+(\\d+)\\s*,\\s*([a-zA-Z]+)\\s+([WBwb])\\s*,\\s*([a-zA-Z]+)\\s+([WBwb])\\s*$");
    private static final Pattern MOVE = Pattern.compile("^\\s*MOVE\\s+(-?\\d+)\\s*,\\s*(-?\\d+)\\s*$");

    public static void main(String[] args) throws Exception {
        System.out.println("Type HELP for commands");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Rules game = null;
        Player p1 = null, p2 = null;
        AiPlayer ai = new AiPlayer();

        while (true) {
            String line = br.readLine();
            if (line == null) break;
            String s = line.trim();
            if (s.equalsIgnoreCase("EXIT")) break;
            if (s.equalsIgnoreCase("HELP")) {
                printHelp();
                continue;
            }

            Matcher mg = GAME.matcher(s);
            Matcher mm = MOVE.matcher(s);

            if (mg.matches()) {
                int n = Integer.parseInt(mg.group(1));
                PlayerType t1 = parseType(mg.group(2));
                Color c1 = Color.fromChar(mg.group(3).charAt(0));
                PlayerType t2 = parseType(mg.group(4));
                Color c2 = Color.fromChar(mg.group(5).charAt(0));
                if (c1 == c2) {
                    System.out.println("Incorrect command");
                    continue;
                }
                p1 = new Player(t1, c1);
                p2 = new Player(t2, c2);
                game = new Rules(n, p1.color);
                System.out.println("New game started");
                autoMovesIfNeeded(game, p1, p2, ai);
                continue;
            }

            if (mm.matches()) {
                if (game == null) {
                    System.out.println("No active game. Use GAME ...");
                    continue;
                }
                int x = Integer.parseInt(mm.group(1));
                int y = Integer.parseInt(mm.group(2));
                try {
                    GameStatus st = game.makeMove(x, y);
                    if (st != GameStatus.IN_PROGRESS) {
                        announce(st);
                        game = null;
                        continue;
                    }
                    autoMovesIfNeeded(game, p1, p2, ai);
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                }
                continue;
            }

            System.out.println("Incorrect command");
        }
    }

    private static void autoMovesIfNeeded(Rules g, Player p1, Player p2, AiPlayer ai) {
        while (true) {
            Player current = (g.nextColor() == p1.color) ? p1 : p2;
            if (current.type == PlayerType.USER) return; // ждём команду MOVE
            Optional<Point> pm = ai.choose(g.board(), g.nextColor());
            if (pm.isEmpty()) {
                announce(GameStatus.DRAW);
                return;
            }
            Point p = pm.get();
            Color mover = g.nextColor();
            GameStatus st = g.makeMove(p.x(), p.y());
            System.out.println(mover + " (" + p.x() + ", " + p.y() + ")");
            if (st != GameStatus.IN_PROGRESS) {
                announce(st);
                break;
            }
        }
    }

    private static void announce(GameStatus st) {
        switch (st) {
            case WIN_W -> System.out.println("Game finished. W wins!");
            case WIN_B -> System.out.println("Game finished. B wins!");
            case DRAW -> System.out.println("Game finished. Draw");
            default -> {
            }
        }
    }

    private static PlayerType parseType(String s) {
        return s.equalsIgnoreCase("user") ? PlayerType.USER : PlayerType.COMP;
    }

    private static void printHelp() {
        System.out.println("Commands:\n  GAME N, user|comp W|B, user|comp W|B\n  MOVE X, Y\n  HELP\n  EXIT");
    }
}
