package primitives;

import java.util.Objects;

public class Ray {
    private Point p0;
    private Vector dir;

    /**
     * constractor
     * @param p0 point
     * @param dir vector
     */
    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalize();
    }

    /**
     * getting p0
     * @return value of point
     */
    public Point getP0() {
        return p0;
    }

    /**
     * getting dir
     * @return value of vector
     */
    public Vector getDir() {
        return dir;
    }

    /**
     * equalsing between two objects
     * @param o Object (basicaly another Point3d) to compare
     * @return true or false accordingly
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return Objects.equals(p0, ray.p0) && Objects.equals(dir, ray.dir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p0, dir);
    }

    /**
     * to string
     * @return value of ray
     */
    @Override
    public String toString() {
        return "Ray{" +
                "p0=" + p0 +
                ", dir=" + dir +
                '}';
    }
}
