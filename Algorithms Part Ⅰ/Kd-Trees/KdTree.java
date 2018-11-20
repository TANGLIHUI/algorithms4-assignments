import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private class Node {
        private Point2D point;
        private Node left;
        private Node right;

        public Node(Point2D point) {
            this.point = point;
            this.left = null;
            this.right = null;
        }
    }

    private Node root = null;
    private int size = 0;

    public KdTree() {
        // construct an empty set of points
    }

    public boolean isEmpty() {
        // is the set empty?
        return root == null;
    }

    public int size() {
        // number of points in the set
        return size;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null)
            throw new IllegalArgumentException();
        if (root == null) {
            root = new Node(p);
            ++size;
        } else {
            insert(p, root, true);
        }
    }

    private void insert(Point2D p, Node n, boolean isPartByX) {
        if (p.equals(n.point))
            return;
        if ((isPartByX ? (p.x() < n.point.x()) : (p.y() < n.point.y()))) {
            if (n.left == null) {
                n.left = new Node(p);
                ++size;
            } else {
                insert(p, n.left, !isPartByX);
            }
        } else {
            if (n.right == null) {
                n.right = new Node(p);
                ++size;
            } else {
                insert(p, n.right, !isPartByX);
            }
        }
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return false;
        return contains(p, root, true);
    }

    private boolean contains(Point2D p, Node n, boolean isPartByX) {
        if (n == null)
            return false;
        if (p.equals(n.point))
            return true;
        if ((isPartByX ? (p.x() < n.point.x()) : (p.y() < n.point.y()))) {
            return contains(p, n.left, !isPartByX);
        } else {
            return contains(p, n.right, !isPartByX);
        }
    }

    public void draw() {
        // draw all points to standard draw
        draw(root, 0.0, 0.0, 1.0, 1.0, true);
    }

    private void draw(Node n, double minx, double miny, double maxx, double maxy, boolean isPartByX) {
        if (n != null) {
            if (isPartByX) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.point.x(), miny, n.point.x(), maxy);
                draw(n.left, minx, miny, n.point.x(), maxy, !isPartByX);
                draw(n.right, n.point.x(), miny, maxx, maxy, !isPartByX);
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(minx, n.point.y(), maxx, n.point.y());
                draw(n.left, minx, miny, maxx, n.point.y(), !isPartByX);
                draw(n.right, minx, n.point.y(), maxx, maxy, !isPartByX);
            }
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> list = new ArrayList<>();
        getPointInRange(root, new RectHV(0.0, 0.0, 1.0, 1.0), rect, list, true);
        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                return list.iterator();
            }
        };
    }

    private void getPointInRange(Node n, RectHV nodeRect, RectHV rect, ArrayList<Point2D> list, boolean isPartByX) {
        if (n != null && nodeRect.intersects(rect)) {
            if (rect.contains(n.point))
                list.add(n.point);
            RectHV lRectHV = new RectHV(nodeRect.xmin(), nodeRect.ymin(), isPartByX ? n.point.x() : nodeRect.xmax(),
                    isPartByX ? nodeRect.ymax() : n.point.y());
            RectHV rRectHV = new RectHV(isPartByX ? n.point.x() : nodeRect.xmin(),
                    isPartByX ? nodeRect.ymin() : n.point.y(), nodeRect.xmax(), nodeRect.ymax());
            getPointInRange(n.left, lRectHV, rect, list, !isPartByX);
            getPointInRange(n.right, rRectHV, rect, list, !isPartByX);
        }
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new IllegalArgumentException();
        return nearest(p, root, Double.MAX_VALUE, new RectHV(0.0, 0.0, 1.0, 1.0), true);
    }

    private Point2D nearest(Point2D p, Node n, double dis, RectHV rect, boolean isPartByX) {
        if (n != null && rect.distanceSquaredTo(p) < dis) {
            Point2D ans = n.point;
            double min = Math.min(dis, n.point.distanceSquaredTo(p));
            RectHV lRectHV = new RectHV(rect.xmin(), rect.ymin(), isPartByX ? n.point.x() : rect.xmax(),
                    isPartByX ? rect.ymax() : n.point.y());
            RectHV rRectHV = new RectHV(isPartByX ? n.point.x() : rect.xmin(), isPartByX ? rect.ymin() : n.point.y(),
                    rect.xmax(), rect.ymax());
            boolean mark = isPartByX ? p.x() < n.point.x() : p.y() < n.point.y();
            Point2D first = nearest(p, mark ? n.left : n.right, min, mark ? lRectHV : rRectHV, !isPartByX);
            if (first != null) {
                double l = first.distanceSquaredTo(p);
                if (l < min) {
                    min = l;
                    ans = first;
                }
            }
            Point2D second = nearest(p, mark ? n.right : n.left, min, mark ? rRectHV : lRectHV, !isPartByX);
            if (second != null) {
                double r = second.distanceSquaredTo(p);
                if (r < min) {
                    min = r;
                    ans = second;
                }
            }
            return ans;
        }
        return null;
    }

    public static void main(String[] args) {
        String filename = "E:/WorkSpace1/algorithm/kdtree/input1M.txt";
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        // StdDraw.setPenColor(StdDraw.RED);
        // StdDraw.setPenRadius(0.03);
        // StdDraw.setPenRadius(0.03);
        int i = 0;
        String t = null;
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            // System.out.println(p);
            // p.draw();
            // t = String.valueOf((char) ('A' + i++));
            // StdDraw.text(x + 0.02, y + 0.02, t);
            kdtree.insert(p);
        }
        in = new In(filename);
        double start = System.currentTimeMillis();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.contains(p);
            // System.out.println(kdtree.contains(p));
        }
        System.out.println((System.currentTimeMillis() - start));
        Point2D p = new Point2D(0.45, 0.63);
        // StdDraw.setPenColor(StdDraw.RED);
        // p.draw();
        // StdDraw.setPenRadius();
        // kdtree.draw();
        System.out.println(kdtree.nearest(p) + "**");
    }
}
