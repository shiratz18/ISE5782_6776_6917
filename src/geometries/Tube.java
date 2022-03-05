package geometries;

import primitives.Double3;
import primitives.Vector;
import primitives.Ray;
import primitives.Point;

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
        return null;
    }
}
