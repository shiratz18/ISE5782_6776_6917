package geometries;

import primitives.Double3;
import primitives.Vector;
import primitives.Ray;
import primitives.Point;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;
/**
 * Tube class represents Tube by ray and radius
 * system
 */
public class Tube extends Geometry{
    protected Ray _axisRay;
    protected double _radius;

    /**
     * constructor
     * @param axisRay ray of tube
     * @param radius radius of tube
     */
    public Tube(Ray axisRay, double radius) {
        _axisRay = axisRay;
        _radius = radius;
    }

    /**
     * getting axis ray
     * @return axis ray
     */
    public Ray getAxisRay() {
        return _axisRay;
    }

    /**
     * getting radius
     * @return radius of tube
     */
    public double getRadius() {
        return _radius;
    }

    /**
     * to string
     * @return values of tube
     */
    @Override
    public String toString() {
        return "Tube{" +
                "_axisRay=" + _axisRay +
                ", _radius=" + _radius +
                '}';
    }

    /**
     * calculating the normal
     * @param point should be null for flat geometries
     * @return normal of tube
     */
    @Override
    public Vector getNormal(Point point) {

        Point P0 = _axisRay.getP0();
        Vector v = _axisRay.getDir();

        Vector P0_P = point.subtract(P0);

        double t = alignZero(v.dotProduct(P0_P));
        //v and P0_P are orthogonal
        if (isZero(t)) {
            return P0_P.normalize();
        }

        Point o = P0.add(v.scale(t));
        //the point is  on the tube axis
        if (point.equals(o)) {
            throw new IllegalArgumentException("point cannot be on the tube axis");
        }

        Vector n = point.subtract(o).normalize();

        return n;
    }

    /**
     *
     * @param ray the ray {@link Ray} that intersect with the graphic object tube
     * @return intersection points
*/
    @Override
    public List<Point> findIntersections(Ray ray) {
        Vector vAxis = _axisRay.getDir();
        Vector v = ray.getDir();
        Point p0 = ray.getP0();

        // At^2+Bt+C=0
        double a = 0;
        double b = 0;
        double c = 0;

        double vVa = alignZero(v.dotProduct(vAxis));
        Vector vVaVa;
        Vector vMinusVVaVa;
        if (vVa == 0) // the ray is orthogonal to the axis
            vMinusVVaVa = v;
        else {
            vVaVa = vAxis.scale(vVa);
            try {
                vMinusVVaVa = v.subtract(vVaVa);
            } catch (IllegalArgumentException e1) { // the rays is parallel to axis
                return null;
            }
        }
        // A = (v-(v*va)*va)^2
        a = vMinusVVaVa.lengthSquared();

        Vector deltaP = null;
        try {
            deltaP = p0.subtract(_axisRay.getP0());
        } catch (IllegalArgumentException e1) { // the ray begins at axis P0
            if (vVa == 0) // the ray is orthogonal to Axis
                return List.of(ray.getPoint(_radius));

            double t = alignZero(Math.sqrt(_radius * _radius / vMinusVVaVa.lengthSquared()));
            return t == 0 ? null : List.of(ray.getPoint(t));
        }

        double dPVAxis = alignZero(deltaP.dotProduct(vAxis));
        Vector dPVaVa;
        Vector dPMinusdPVaVa;
        if (dPVAxis == 0)
            dPMinusdPVaVa = deltaP;
        else {
            dPVaVa = vAxis.scale(dPVAxis);
            try {
                dPMinusdPVaVa = deltaP.subtract(dPVaVa);
            } catch (IllegalArgumentException e1) {
                double t = alignZero(Math.sqrt(_radius * _radius / a));
                return t == 0 ? null : List.of(ray.getPoint(t));
            }
        }

        // B = 2(v - (v*va)*va) * (dp - (dp*va)*va))
        b = 2 * alignZero(vMinusVVaVa.dotProduct(dPMinusdPVaVa));
        c = dPMinusdPVaVa.lengthSquared() - _radius * _radius;

        // A*t^2 + B*t + C = 0 - lets resolve it
        double discr = alignZero(b * b - 4 * a * c);
        if (discr <= 0) return null; // the ray is outside or tangent to the tube

        double doubleA = 2 * a;
        double tm = alignZero(-b / doubleA);
        double th = Math.sqrt(discr) / doubleA;
        if (isZero(th)) return null; // the ray is tangent to the tube

        double t1 = alignZero(tm + th);
        if (t1 <= 0) // t1 is behind the head
            return null; // since th must be positive (sqrt), t2 must be non-positive as t1

        double t2 = alignZero(tm - th);

        // if both t1 and t2 are positive
        if (t2 > 0)
            return List.of(ray.getPoint(t1), ray.getPoint(t2));
        else // t2 is behind the head
            return List.of(ray.getPoint(t1));
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        return null;
    }
/**


/**
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> intersections = new LinkedList<GeoPoint>();
        Vector vAxis = _axisRay.getDir();
        Vector v = ray.getDir();
        Point p0 = ray.getP0();

        // At^2+Bt+C=0
        double a = 0;
        double b = 0;
        double c = 0;

        double vVa = alignZero(v.dotProduct(vAxis));
        Vector vVaVa;
        Vector vMinusVVaVa;
        if (vVa == 0) // the ray is orthogonal to the axis
            vMinusVVaVa = v;
        else {
            vVaVa = vAxis.scale(vVa);
            try {
                vMinusVVaVa = v.subtract(vVaVa);
            } catch (IllegalArgumentException e1) { // the rays is parallel to axis
                return null;
            }
        }
        // A = (v-(v*va)*va)^2
        a = vMinusVVaVa.lengthSquared();

        Vector deltaP = null;
        try {
            deltaP = p0.subtract(_axisRay.getP0());
        } catch (IllegalArgumentException e1) { // the ray begins at axis P0
            if (vVa == 0) // the ray is orthogonal to Axis
                return List.of(new GeoPoint(this,ray.getPoint(_radius)));

            double t = alignZero(Math.sqrt(_radius * _radius / vMinusVVaVa.lengthSquared()));
            return t == 0 ? null : List.of(new GeoPoint(this,ray.getPoint(_radius)));
        }

        double dPVAxis = alignZero(deltaP.dotProduct(vAxis));
        Vector dPVaVa;
        Vector dPMinusdPVaVa;
        if (dPVAxis == 0)
            dPMinusdPVaVa = deltaP;
        else {
            dPVaVa = vAxis.scale(dPVAxis);
            try {
                dPMinusdPVaVa = deltaP.subtract(dPVaVa);
            } catch (IllegalArgumentException e1) {
                double t = alignZero(Math.sqrt(_radius * _radius / a));
                return t == 0 ? null : List.of(new GeoPoint(this,ray.getPoint(_radius)));
            }
        }

        // B = 2(v - (v*va)*va) * (dp - (dp*va)*va))
        b = 2 * alignZero(vMinusVVaVa.dotProduct(dPMinusdPVaVa));
        c = dPMinusdPVaVa.lengthSquared() - _radius * _radius;

        // A*t^2 + B*t + C = 0 - lets resolve it
        double discr = alignZero(b * b - 4 * a * c);
        if (discr <= 0) return null; // the ray is outside or tangent to the tube

        double doubleA = 2 * a;
        double tm = alignZero(-b / doubleA);
        double th = Math.sqrt(discr) / doubleA;
        if (isZero(th)) return null; // the ray is tangent to the tube

        double t1 = alignZero(tm + th);
        if (t1 <= 0) // t1 is behind the head
            return null; // since th must be positive (sqrt), t2 must be non-positive as t1

        double t2 = alignZero(tm - th);

        // if both t1 and t2 are positive
        if (t2 > 0)
            return List.of(new GeoPoint(this,ray.getPoint(_radius)),new GeoPoint(this,ray.getPoint(_radius)));
        else // t2 is behind the head
            return List.of(new GeoPoint(this,ray.getPoint(_radius)));
    }
    */
}

