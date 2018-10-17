import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        Point[] copy = points.clone();
        Arrays.sort(copy);
        if (hasDoubleOrNull(copy)) {
            throw new IllegalArgumentException();
        }
        double slope;
        for (int i = 0; i < copy.length - 3; i++) {
            for (int j = i + 1; j < copy.length - 2; j++) {
                slope = copy[i].slopeTo(copy[j]);
                for (int k = j + 1; k < copy.length - 1; k++) {
                    if (slope == copy[j].slopeTo(copy[k])) {
                        for (int l = k + 1; l < copy.length; l++) {
                            if (slope == copy[k].slopeTo(copy[l])) {
                                segments.add(new LineSegment(copy[i], copy[l]));
                            }
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    private boolean hasDoubleOrNull(Point[] points) {
        if (points[0] == null) throw new IllegalArgumentException();
        for (int i = 1; i < points.length; i++) {
            if (points[i] == null || points[i].compareTo(points[i - 1]) == 0) {
                return true;
            }
        }
        return false;
    }
}
