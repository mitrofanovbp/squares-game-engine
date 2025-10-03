package com.ivk.squares.engine;

import java.util.*;

public class Board {
    private final int size;
    private final Map<Point, Color> cells = new HashMap<>();

    public Board(int size) {
        if (size < 3) throw new IllegalArgumentException("Board size must be > 2");
        this.size = size;
    }

    public int size() {
        return size;
    }

    public Optional<Color> get(Point p) {
        return Optional.ofNullable(cells.get(p));
    }

    public boolean isInside(Point p) {
        return p.x() >= 0 && p.y() >= 0 && p.x() < size && p.y() < size;
    }

    public boolean isEmpty(Point p) {
        return !cells.containsKey(p);
    }

    public void place(Move m) {
        Point p = new Point(m.x(), m.y());
        if (!isInside(p)) throw new IllegalArgumentException("Out of board");
        if (!isEmpty(p)) throw new IllegalArgumentException("Cell is occupied");
        cells.put(p, m.color());
    }

    public Set<Point> pointsOf(Color c) {
        Set<Point> s = new HashSet<>();
        for (var e : cells.entrySet()) if (e.getValue() == c) s.add(e.getKey());
        return s;
    }

    public boolean isFull() {
        return cells.size() >= size * size;
    }

    public List<Point> emptyCells() {
        List<Point> r = new ArrayList<>();
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++) {
                Point p = new Point(x, y);
                if (!cells.containsKey(p)) r.add(p);
            }
        return r;
    }
}
