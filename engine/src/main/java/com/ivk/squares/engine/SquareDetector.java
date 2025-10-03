package com.ivk.squares.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SquareDetector {
    public static boolean hasSquare(Set<Point> points) {
        if (points.size() < 4) return false;
        List<Point> list = new ArrayList<>(points);
        Set<Point> set = new HashSet<>(points);
        for (int i = 0; i < list.size(); i++) {
            Point p = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                Point q = list.get(j);
                int dx = q.x() - p.x();
                int dy = q.y() - p.y();
                Point r = new Point(-dy, dx);                               // +90°
                Point p3 = new Point(p.x() + r.x(), p.y() + r.y());
                Point p4 = new Point(q.x() + r.x(), q.y() + r.y());
                if (set.contains(p3) && set.contains(p4)) return true;
                Point p3b = new Point(p.x() - r.x(), p.y() - r.y());  // −90°
                Point p4b = new Point(q.x() - r.x(), q.y() - r.y());
                if (set.contains(p3b) && set.contains(p4b)) return true;
            }
        }
        return false;
    }
}
