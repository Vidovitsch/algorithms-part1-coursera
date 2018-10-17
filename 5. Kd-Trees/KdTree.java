import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.ArrayList;

public class KdTree {

    private Node root = null;
    private int size = 0;

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double[] rectPoints = {0, 0, 1, 1};
        root = insert(root, p, rectPoints, true);
    }

    private Node insert(Node node, Point2D key, double[] rectPoints, boolean isVertical) {
        if (node == null) {
            size++;
            return new Node(key, new RectHV(rectPoints[0], rectPoints[1], rectPoints[2], rectPoints[3]));
        }
        if (!node.point.equals(key)) {
            double comp = isVertical ? key.x() - node.point.x() : key.y() - node.point.y();
            if (comp < 0) {
                if (isVertical) rectPoints[2] = node.point.x();
                else            rectPoints[3] = node.point.y();
                node.left = insert(node.left, key, rectPoints, !isVertical);
            } else {
                if (isVertical) rectPoints[0] = node.point.x();
                else            rectPoints[1] = node.point.y();
                node.right = insert(node.right, key, rectPoints, !isVertical);
            }
        }
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(root, p, true);
    }

    private boolean contains(Node node, Point2D key, boolean isVertical) {
        if (node == null) return false;
        double comp = isVertical ? key.x() - node.point.x() : key.y() - node.point.y();
        if (node.point.equals(key)) return true;
        if (comp < 0)   return contains(node.left, key, !isVertical);
        else            return contains(node.right, key, !isVertical);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, true);
    }

    private void draw(Node node, boolean isVertical) {
        if (node == null) return;
        StdDraw.setPenColor(Color.BLACK);
        node.point.draw();
        double x = node.point.x(), y = node.point.y();
        if (isVertical) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(x, node.rect.ymin(), x, node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(node.rect.xmin(), y, node.rect.xmax(), y);
        }
        draw(node.left, !isVertical);
        draw(node.right, !isVertical);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> inRange = new ArrayList<Point2D>();
        range(root, rect, true, inRange);
        return inRange;
    }

    private void range(Node node, RectHV rect, boolean isVertical, ArrayList<Point2D> inRange) {
        if (node == null || !node.rect.intersects(rect)) return;
        if (rect.contains(node.point)) inRange.add(node.point);
        range(node.left, rect, !isVertical, inRange);
        range(node.right, rect, !isVertical, inRange);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return nearest(root, p, true, root, Double.POSITIVE_INFINITY).point;
    }

    private Node nearest(Node node, Point2D query, boolean isVertical, Node nearestNode, double nearestDistance) {
        if (node == null) return nearestNode;
        double currentDistance = node.point.distanceSquaredTo(query);
        if (currentDistance < nearestDistance) {
            nearestNode = node;
            nearestDistance = currentDistance;
        }
        double comp = isVertical ? query.x() - node.point.x() : query.y() - node.point.y();
        Node closerToQuery = comp < 0 ? node.left : node.right;
        nearestNode = nearest(closerToQuery, query, !isVertical, nearestNode, nearestDistance);
        Node furtherFromQuery = comp > 0 ? node.left : node.right;
        if (furtherFromQuery != null) {
            double distanceFromRect = furtherFromQuery.rect.distanceSquaredTo(query);
            nearestDistance = nearestNode.point.distanceSquaredTo(query);
            if (distanceFromRect < nearestDistance) {
                Node possiblyNearestNode = nearest(furtherFromQuery, query, !isVertical, nearestNode, nearestDistance);
                if (possiblyNearestNode != nearestNode &&
                        possiblyNearestNode.point.distanceSquaredTo(query) < nearestDistance) {
                    nearestNode = possiblyNearestNode;
                }
            }
        }
        return nearestNode;
    }

    private final class Node {

        private final Point2D point;
        private final RectHV rect;
        private Node left = null;
        private Node right = null;

        public Node(Point2D p, RectHV r) {
            point = p;
            rect = r;
        }
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.2, 0.4));
        tree.insert(new Point2D(0.2, 0.5));
        tree.insert(new Point2D(0.2, 0.6));
        System.out.println(tree.contains(new Point2D(0.2, 0.5)));
    }
}
