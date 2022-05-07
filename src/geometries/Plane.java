package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.*;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * TODO place description here
 */
public class Plane extends Geometry {
    private Point _q0;
    private Vector _normal;

    /**
     * constructor
     * @param q0 point
     * @param normal vector of normal
     */
    public Plane(Point q0, Vector normal) {
        _q0 = q0;
        _normal = normal.normalize();
    }

    /**
     * constructor
     * @param p1 point 1
     * @param p2 point 2
     * @param p3 point 3
     */
    public Plane(Point p1, Point p2, Point p3) {
        _q0=p1;
        //calculate the normal of plane
       _normal=(p2.subtract(p1).crossProduct(p3.subtract(p1))).normalize();
    }

    /**
     * getting q0
     * @return this point
     */
    public Point getQ0() {
        return _q0;
    }

    /**
     * getting normal
     * @return normal of plane
     */
    public Vector getNormal() {
        return _normal;
    }

    /**
     * to string
     * @return values of plane
     */
    @Override
    public String toString() {
        return "Plane{" +
                "_q0=" + _q0 +
                ", _normal=" + _normal +
                '}';
    }

    /**
     * calculating the normal of plane
     * @param p point
     * @return vector of the normal
     */
    public Vector getNormal(Point p)
    {
       return _normal;
    }

    /**
     *finding all intersection points by checking every case
     * @param ray the ray {@link Ray} that intersect with the graphic object
     * @return list of intersection points
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Point p0 = ray.getP0();
        Vector v = ray.getDir();
        Vector n = _normal;

        double nv = n.dotProduct(v);
        //ray parallel to plane or ray begins in the same point which appears as the plane's reference point
        if (isZero(nv) || _q0.equals(p0))
            return null;
        double nQMinusP0 = n.dotProduct(_q0.subtract(p0));
        double t = alignZero(nQMinusP0 / nv);
        if (t > 0 && alignZero(t - maxDistance) <= 0) {
            Point p = ray.getPoint(t);
            return List.of(new GeoPoint(this, p));
        }
        //t<=0
        return null;
    }

}
