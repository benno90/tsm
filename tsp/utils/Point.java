package utils;

/**
 *
 * @author Marek Arnold (arnd@zhaw.ch)
 *
 * A point in an euclidean 2D space.
 */
public class Point implements Comparable<Point> {

    private final int id;
    private final double x, y;

    /**
     * Instantiate a new point with the given coordinates.
     *
     * @param id The id of this point. Ids among points of the same problem
     * should be distinct.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Point(int id, double x, double y) {
        super();
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        super();
        this.id = p.getId();
        this.x = p.getX();
        this.y = p.getY();
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean equals(Point p) {
        return this.id == p.id;
    }

    public boolean equalsGeom(Point p) {
        return ((this.x == p.x) && (this.y == p.y)) && (this.id != p.id);
    }

    @Override
    public int compareTo(Point p) {
        return Integer.compare(this.id, p.id);
    }
}
