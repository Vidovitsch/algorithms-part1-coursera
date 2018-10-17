import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new NullPointerException();
        }

        Point[] copy = points.clone();
        Arrays.sort(copy);

        if (hasDoubleOrNull(copy)) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < copy.length - 3; i++) {
            Arrays.sort(copy);
            Arrays.sort(copy, copy[i].slopeOrder());

            for (int j = 0, first = 1, last = 2; last < copy.length; last++) {
                while (last < copy.length && Double.compare(copy[j].slopeTo(copy[first]), copy[j].slopeTo(copy[last])) == 0) {
                    last++;
                }
                if (last - first >= 3 && copy[j].compareTo(copy[first]) < 0) {
                    segments.add(new LineSegment(copy[j], copy[last - 1]));
                }
                first = last;
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
