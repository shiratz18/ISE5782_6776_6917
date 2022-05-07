package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import primitives.Point;

import static primitives.Util.alignZero;
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
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> result = _plane.findGeoIntersectionsHelper(ray, maxDistance);

        //Check if the ray intersect the plane.
        if (result == null) {
            return null;
        }
        for (GeoPoint g : result)
            g._geometry=this;

        Vector v1 = _vertices.get(0).subtract(ray.getP0());
        Vector v2 = _vertices.get(1).subtract(ray.getP0());
        Vector v3 = _vertices.get(2).subtract(ray.getP0());

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        Vector v = ray.getDir();

        double vn1 = alignZero(v.dotProduct(n1));
        double vn2 = alignZero(v.dotProduct(n2));
        double vn3 = alignZero(v.dotProduct(n3));

        //The point is inside if all 𝒗 ∙ 𝒏𝒊 have the same sign (+/-)
        if ((vn1 > 0 && vn2 > 0 && vn3 > 0) || (vn1 < 0 && vn2 < 0 && vn3 < 0)) {
            return result;
        }
        return null;
    }


}
