package geometries;

import primitives.Double3;
import primitives.Vector;
import primitives.Ray;
import primitives.Point;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Tube implements Geometry{
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

        if (isZero(t)) {
            return P0_P.normalize();
        }

        Point o = P0.add(v.scale(t));

        if (point.equals(o)) {
            throw new IllegalArgumentException("point cannot be on the tube axis");
        }

        Vector n = point.subtract(o).normalize();

        return n;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}

