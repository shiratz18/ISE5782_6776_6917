package primitives;

import java.util.List;
import java.util.Objects;
import geometries.Intersectable.GeoPoint;
// this class presents ray between point to point
public class Ray {
    private Point _p0;
    private Vector _dir;

    /**
     * constructor
     * @param p0 point
     * @param dir vector
     */
    public Ray(Point p0, Vector dir) {
        this._p0 = p0;
        this._dir = dir.normalize();
    }

    //parameter for size of first moving rays for shading rays
    private static final double DELTA = 0.1;

    /**
     * constructor for Ray
     * set the ray with the sliding of
     * the initial point in the delta on the normal
     * @param p the initial point
     * @param dir the direction of the ray
     * @param n the normal
     */
    public Ray(Point p, Vector dir, Vector n) {
        //point + normal.scale(±DELTA)
        _dir = dir.normalize();

        double nv = n.dotProduct(_dir);

        Vector normalEpsilon = n.scale((nv > 0 ? DELTA : -DELTA));
        _p0 = p.add(normalEpsilon);
    }


    /**
     * getting p0
     * @return value of point
     */
    public Point getP0() {
        return _p0;
    }

    /**
     * getting dir
     * @return value of vector
     */
    public Vector getDir() {
        return _dir;
    }

    /**
     * equaling between two objects
     * @param o Object (basically another Point3d) to compare
     * @return true or false accordingly
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return _p0.equals(ray._p0) && _dir.equals(ray._dir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_p0, _dir);
    }

    /**
     * to string
     * @return value of ray
     */
    @Override
    public String toString() {
        return "Ray{" +
                "p0=" + _p0 +
                ", dir=" + _dir +
                '}';
    }

    /**
     * calculate the new point that got by the scale t
     * @param t the scale
     * @return new point by the scale
     */
    public Point getPoint(double t) {
        //if the scale is 0 the point doesn't change
        if(t==0) {
            return _p0;
        }
        //the scale isn't 0 so the point is starting point plus the vector multiplicative the scale
        return _p0.add(_dir.scale(t));

    }
    /**
     * find the closest Point to Ray origin
     * @param points intersections point List
     * @return closest point
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList())._point;
    }

    public GeoPoint findClosestGeoPoint (List<GeoPoint> geoPointsList){
        GeoPoint result =null;
        double closestDistance = Double.MAX_VALUE;
        //There aren't points in the list
        if(geoPointsList== null){
            return null;
        }
        //move on the points
        for (GeoPoint p: geoPointsList) {
            double temp = p._point.distance(_p0);
            //if found a point is closer
            if(temp < closestDistance){
                closestDistance =temp;
                result =p;
            }
        }

        return  result;
    }

}
