package geometries;

import primitives.Double3;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    public void testGetNormal()
    {
        // ============ Equivalence Partitions Tests ==============
        //  There is a simple single test here
        Triangle pl = new Triangle(new Double3(0, 0, 1), new Double3(1, 0, 0), new Double3(0, 1, 0));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals(new Vector(sqrt3, sqrt3, sqrt3),
                pl.getNormal(new Point(0, 0, 1)),
                "Bad normal to triangle");
    }
}

