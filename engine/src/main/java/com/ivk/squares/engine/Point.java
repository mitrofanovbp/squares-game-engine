package com.ivk.squares.engine;

public record Point(int x, int y) implements Comparable<Point> {
    @Override
    public int compareTo(Point o) {
        int c = Integer.compare(this.y, o.y);
        return c != 0 ? c : Integer.compare(this.x, o.x);
    }
}
