package geometries;

import primitives.*;
import java.util.List;

import primitives.Point;

public class Triangle extends Polygon{
    /**
     * constructor
     * @param c1 coordinate value for X axis
     * @param c2 coordinate value for Y axis
     * @param c3 coordinate value for Z axis
     */
    public Triangle(Double3 c1, Double3 c2, Double3 c3) {
        super(new Point(c1), new Point(c2), new Point(c3));
    }
}
