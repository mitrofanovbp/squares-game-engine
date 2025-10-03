package com.ivk.squares.engine;

import java.util.*;

public class AiPlayer {
    public Optional<Point> choose(Board board, Color me) {
        List<Point> empties = board.emptyCells();
        // 1) Winning move
        for (Point p : empties) {
            Board copy = copy(board);
            copy.place(new Move(p.x(), p.y(), me));
            if (SquareDetector.hasSquare(copy.pointsOf(me))) return Optional.of(p);
        }
        // 2) Block opponent
        Color op = me.other();
        for (Point p : empties) {
            Board copy = copy(board);
            copy.place(new Move(p.x(), p.y(), op));
            if (SquareDetector.hasSquare(copy.pointsOf(op))) return Optional.of(p);
        }
        // 3) Heuristic: potential + center bias
        Point best = null;
        int bestScore = Integer.MIN_VALUE;
        double cx = (board.size() - 1) / 2.0, cy = (board.size() - 1) / 2.0;
        for (Point p : empties) {
            int score = potentialAfter(board, p, me) - potentialAfter(board, p, op);
            double dist = Math.hypot(p.x() - cx, p.y() - cy);
            int centerBias = (int) Math.round(100 - dist * 10);
            int total = score + centerBias;
            if (total > bestScore) {
                bestScore = total;
                best = p;
            }
        }
        return Optional.ofNullable(best);
    }

    private int potentialAfter(Board b, Point p, Color c) {
        Board copy = copy(b);
        copy.place(new Move(p.x(), p.y(), c));
        Set<Point> pts = copy.pointsOf(c);
        int count = 0;
        List<Point> list = new ArrayList<>(pts);
        Set<Point> set = new HashSet<>(pts);
        for (int i = 0; i < list.size(); i++) {
            Point a = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                Point q = list.get(j);
                int dx = q.x() - a.x();
                int dy = q.y() - a.y();
                Point r = new Point(-dy, dx);
                Point p3 = new Point(a.x() + r.x(), a.y() + r.y());
                Point p4 = new Point(q.x() + r.x(), q.y() + r.y());
                if (set.contains(p3) ^ set.contains(p4)) count++;
                Point p3b = new Point(a.x() - r.x(), a.y() - r.y());
                Point p4b = new Point(q.x() - r.x(), q.y() - r.y());
                if (set.contains(p3b) ^ set.contains(p4b)) count++;
            }
        }
        return count;
    }

    private Board copy(Board b) {
        Board nb = new Board(b.size());
        for (Point p : b.pointsOf(Color.W)) nb.place(new Move(p.x(), p.y(), Color.W));
        for (Point p : b.pointsOf(Color.B)) nb.place(new Move(p.x(), p.y(), Color.B));
        return nb;
    }
}
