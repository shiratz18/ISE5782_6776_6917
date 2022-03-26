package geometries;
import primitives.*;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Sphere class represents Sphere by point in 3D Cartesian coordinate and radius
 * system
 */
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
    /**
     *finding all intersection points by checking every case
     * @param ray the ray {@link Ray} that intersect with the graphic object
     * @return list of intersection points
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point P0 = ray.getP0();
        Vector v = ray.getDir();
        //if the center and po equals there is one point from the center by radius
        if (P0.equals(_center)) {
            return List.of(_center.add(v.scale(_radius)));
        }

        Vector U = _center.subtract(P0);

        double tm = alignZero(v.dotProduct(U));
        double d = alignZero(Math.sqrt(U.lengthSquared() - tm * tm));

        // no intersections : the ray direction is above the sphere
        if (d >= _radius) {
            return null;
        }

        double th = alignZero(Math.sqrt(_radius * _radius - d * d));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);
        //There are 2 intersections points
        if (t1 > 0 && t2 > 0) {
            Point P1 =ray.getPoint(t1);
            Point P2 =ray.getPoint(t2);
            return List.of(P1, P2);
        }
        //There is one intersection point
        if (t1 > 0) {
            Point P1 =ray.getPoint(t1);
            return List.of(P1);
        }
        //There is one intersection point
        if (t2 > 0) {
//            Point3D P2 = P0.add(v.scale(t2));
            Point P2 =ray.getPoint(t2);
            return List.of(P2);
        }
        return null;
    }
}
