import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private TreeSet<Point2D> points = new TreeSet<>();

    public PointSET() {
        // construct an empty set of points
    }

    public boolean isEmpty() {
        // is the set empty?
        return points.isEmpty();
    }

    public int size() {
        // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new IllegalArgumentException();
        points.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null)
            throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> list = new ArrayList<>();
        for (Point2D p : points) {
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax() && p.y() >= rect.ymin() && p.y() <= rect.ymax()) {
                list.add(p);
            }
        }
        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                return list.iterator();
            }
        };
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new IllegalArgumentException();
        if (points.isEmpty())
            return null;
        Point2D ans = null;
        double min = Double.MAX_VALUE;
        for (Point2D point : points) {
            double cur = p.distanceSquaredTo(point);
            if (min > cur) {
                min = cur;
                ans = point;
            }
        }
        return ans;
    }

    public static void main(String[] args) {

    }
}
