package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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

    /**
     *finding all intersection points by checking every case
     * @param ray the ray {@link Ray} that intersect with the graphic object
     * @return list of intersection points
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point P0 = ray.getP0();
        Vector v = ray.getDir();

        Vector n = _normal;
        //The points are the same so there is no intersections
        if(_q0.equals(P0)){
            return  null;
        }

        Vector P0_Q0 = _q0.subtract(P0);

        //numerator
        double nP0Q0  = alignZero(n.dotProduct(P0_Q0));

        // P0 is on the plane
        if (isZero(nP0Q0 )){
            return null;
        }

        //denominator
        double nv = alignZero(n.dotProduct(v));

         // ray is lying on the plane: infinite numbers of intersection points
        if(isZero(nv)){
            return null;
        }

        double  t = alignZero(nP0Q0  / nv);
        // if(t < 0 the ray doesn't point towards the plane )
        if (t <=0){
            return  null;
        }

        Point point = ray.getPoint(t);

        return List.of(point);
    }

}
