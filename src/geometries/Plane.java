package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * TODO place description here
 */
public class Plane implements Geometry {
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

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point P0 = ray.getP0();
        Vector v = ray.getDir();
        Vector n = _normal;

        double nv = n.dotProduct(v);

        // ray is lying on the plane: infinite numbers of intersection points
        if (isZero(nv)){
            return null;
        }

        Vector Q0_P0 = P0.subtract(_q0);

        double numerator = n.dotProduct(Q0_P0);

        // P0 is on the plane
        if(numerator == 0) {
            return null;
        }

        //distance to reach P from ray origin
        double t = alignZero(numerator/nv);

        // if(t < 0 the ray doesn't point towards the plane )
        if(t > 0){
            Point P = ray.getPoint(t);//P0.add(v.scale(t));
            return List.of(P);
        }
        return null;
    }
}
