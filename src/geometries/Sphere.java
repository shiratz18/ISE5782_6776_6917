package geometries;
import primitives.*;

public class Sphere implements Geometry{
    private Point _center;
    private double _radius;

    /**
     * constructor
     * @param center point
     * @param radius radius of sphere
     */
    public Sphere(Point center, double radius) {
        _center = center;
        _radius = radius;
    }

    /**
     * getting center
     * @return center of sphere
     */
    public Point getCenter() {
        return _center;
    }

    /**
     * getting radius
     * @return radius of sphere
     */
    public double getRadius() {
        return _radius;
    }

    /**
     * to string
     * @return values of sphere
     */
    @Override
    public String toString() {
        return "Sphere{" +
                "_center=" + _center +
                ", _radius=" + _radius +
                '}';
    }

    /**
     * calculating the normal of sphere
     * @param point should be null for flat geometries
     * @return the noemal
     */
    @Override
    public Vector getNormal(Point point) {
        Vector p0_p = _center.subtract(point);
        return p0_p.normalize();
    }
}
