package primitives;

import java.util.Objects;
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
}
