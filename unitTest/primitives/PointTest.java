package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    Vector v1= new Vector(1,2,3);
    Vector v2= new Vector(-2,-4,-6);
    Vector v3= new Vector(0, 3, 4);




    @Test
    void testDistanceSquared() {
        //Simple test of length squared
        assertEquals(14d, v1.lengthSquared(), 0.00001, "lengthSquared() wrong value");
    }

    @Test
    void testDistance() {
        //Simple test of length
        assertEquals(5d, v3.length(), "length() wrong value");
    }
}