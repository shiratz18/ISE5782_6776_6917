package primitives;

import java.util.Objects;

public class Point {
   final protected Double3 xyz;

    public Point(double x, double y, double z) {
        xyz = new Double3(x,y,z);
    }

    public Point(Double3 d){xyz=d;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return xyz.equals(point.xyz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xyz);
    }

    @Override
    public String toString() {
        return "Point{" +
                "xyz=" + xyz +
                '}';
    }

    public Point add(Vector vector) {
        return new Point(xyz.d1+ vector.xyz.d1,xyz.d2+ vector.xyz.d2,xyz.d3+ vector.xyz.d3);
    }
    public Vector subtract(Point point) {
        Double3 newXyz = xyz.subtract(point.xyz);
        if(Double3.ZERO.equals(newXyz)) {
            throw  new IllegalArgumentException("subtract resulting ZERO vector - not allowed");
        }
        return new Vector(newXyz.d1, newXyz.d2, newXyz.d3);
    }

    public Double3 getXyz() {
        return xyz;
    }
}
