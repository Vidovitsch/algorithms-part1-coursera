import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> rbTree;

    // construct an empty set of points
    public PointSET() {
        rbTree = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return rbTree.isEmpty();
    }

    // number of points in the set
    public int size() {
        return rbTree.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        rbTree.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return rbTree.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        Iterator<Point2D> iterator = rbTree.iterator();
        while (iterator.hasNext()) {
            iterator.next().draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> inRange = new ArrayList<Point2D>();
        Iterator<Point2D> iterator = rbTree.iterator();
        while (iterator.hasNext()) {
            Point2D p = iterator.next();
            if (rect.contains(p)) inRange.add(p);
        }
        return inRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (size() > 0) {
            Iterator<Point2D> iterator = rbTree.iterator();
            Point2D nearest = iterator.next();
            while (iterator.hasNext()) {
                Point2D point = iterator.next();
                if (p.distanceSquaredTo(point) < p.distanceSquaredTo(nearest)) nearest = point;
            }
            return nearest;
        }
        return null;
    }
}

