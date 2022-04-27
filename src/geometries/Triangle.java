package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import primitives.Point;

import static primitives.Util.isZero;

/**
 * Triangle class represent a triangle by polygon in 3D Cartesian coordinate
 * system
 */
public class Triangle extends Polygon {
    /**
     * constructor
     *
     * @param c1 coordinate value for X axis
     * @param c2 coordinate value for Y axis
     * @param c3 coordinate value for Z axis
     */
    public Triangle(Point c1, Point c2, Point c3) {
        super(c1, c2, c3);
    }

    /**
     * finding all intersection points by checking every case
     *
     * @param ray the ray {@link Ray} that intersect with the graphic object
     * @return list of intersection points
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> intersections = new LinkedList<GeoPoint>();
        //option 1
        Point p0 = ray.getP0(); //the start ray point
        Vector v = ray.getDir();

        intersections = _plane.findGeoIntersections(ray);
        for(GeoPoint g:intersections)
            g._geometry=this;
        //There aren't intersection points
        if (intersections == null)
            return null;
        //vectors from the ray start point to the polygon vertices
        Vector v1 = _vertices.get(0).subtract(p0);
        Vector v2 = _vertices.get(1).subtract(p0);
        Vector v3 = _vertices.get(2).subtract(p0);

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double nv1 = v.dotProduct(n1);
        double nv2 = v.dotProduct(n2);
        double nv3 = v.dotProduct(n3);
        //The point is out of triangle
        if (isZero(nv1)) return null;
        if (isZero(nv2)) return null;
        if (isZero(nv3)) return null;

        return ((nv1 > 0 && nv2 > 0 && nv3 > 0) || (nv1 < 0 && nv2 < 0 && nv3 < 0)) ? intersections : null;

    }
}
