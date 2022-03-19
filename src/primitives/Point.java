package primitives;

import java.util.Objects;

public class Point {
    Double3 _xyz;

    /**
     *getting point
     * @return _xyz the value of point
     */
    public Double3 getXyz() {
        return _xyz;
    }

    /**
     * constructor
     * @param x coordinate value for X axis
     * @param y coordinate value for Y axis
     * @param z coordinate value for Z axis
     */
    public Point(double x, double y, double z) {
        _xyz = new Double3(x,y,z);
    }

    /**
     * constructor
     * @param d Double3
     */
    public Point(Double3 d){_xyz=d;}

    /**
     * getting x
     * @return coordinate value for X axis
     */
    public double getX()
    {
        return _xyz.d1;
    }

    /**
     * getting y
     * @return coordinate value for Y axis
     */
    public double getY()
    {
        return _xyz.d2;
    }

    /**
     * getting z
     * @return coordinate value for Z axis
     */
    public double getZ()
    {
        return _xyz.d3;
    }
    /**
     * checking if the object are the same
     * @param o Object (basicaly another Point3d) to compare
     * @return true or false accordingly
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return _xyz.equals(point._xyz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_xyz);
    }

    /**
     * to string
     * @return the values of point
     */
    @Override
    public String toString() {
        return "Point{" +
                "xyz=" + _xyz +
                '}';
    }

    /**
     *
     * the function calculates vector plus point
     * @param vector
     * @return new point of the result
     */
    public Point add(Vector vector) {
        Double3 newD=_xyz.add(vector._xyz);
        return new Point(newD.d1, newD.d2,newD.d3);
    }

    /**
     *
     * the function calculates point minus point
     * @param point
     * @return new point of the result
     */
    public Vector subtract(Point point) {
        Double3 newXyz = point._xyz.subtract(_xyz);
        if(Double3.ZERO.equals(newXyz)) {
            throw  new IllegalArgumentException("subtract resulting ZERO vector - not allowed");
        }
        return new Vector(newXyz.d1, newXyz.d2, newXyz.d3);
    }

    /**
     * the function calculates the distance by minus between them and pow 2 every value
     * @param point
     * @return the result of the distance in pow 2
     */
     public double distanceSquared(Point point)
     {
        Vector newV= point.subtract(this);
         return newV._xyz.d1*newV._xyz.d1+newV._xyz.d2*newV._xyz.d2+newV._xyz.d3*newV._xyz.d3;
     }

    /**
     *the function calculates the distance between 2 points by making root
     *  to the value that return from distance squared
     * @param point
     * @return the distance between 2 points
     */
     public double distance(Point point)
     {
       return Math.sqrt(this.distanceSquared(point));
     }

}
