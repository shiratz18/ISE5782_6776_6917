package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Tests for Vector
 *
 * @author Shirel
 */
class VectorTest{
    Vector v1= new Vector(1,2,3);
    Vector v2= new Vector(-2,-4,-6);
    Vector v3 = new Vector(0, 3, -2);
    Vector v4= new Vector(0, 3, 4);


    @Test
    void testDotProduct() {
        //Simple dotProduct test
        assertEquals(-28d, v1.dotProduct(v2), 0.00001,"dotProduct() wrong value");
        // dotProduct for orthogonal vectors
        assertEquals(0d, v1.dotProduct(v3), 0.00001, "dotProduct() for orthogonal vectors is not zero");
    }



    @Test
    void testNormalize() {
        // Simple test of normalizing
        Vector n = v4.normalize();
        assertFalse(v4 == n, "normalized() changes the vector itself");

        assertEquals(1d, n.lengthSquared(), 0.00001, "wrong normalized vector length");
        assertThrows(IllegalArgumentException.class,
                () -> v4.crossProduct(n),
                "normalized vector is not in the same direction");
        assertEquals(new Vector(0, 0.6, 0.8), n, "wrong normalized vector");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */

    @Test
    void testCrossProduct() {

        // ============ Equivalence Partitions Tests ==============
        Vector v3 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v3);

        // TC01: Test that length of cross-product is proper (orthogonal vectors taken
        // for simplicity)
        assertEquals( v1.length() * v3.length(), vr.length(), 0.00001, "crossProduct() wrong result length");

        // TC02: Test cross-product result orthogonality to its operands
        assertTrue(isZero(vr.dotProduct(v1)),"crossProduct() result is not orthogonal to 1st operand");
        assertTrue(isZero(vr.dotProduct(v2)),"crossProduct() result is not orthogonal to 2nd operand");

        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from cross-product of co-lined vectors

        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v2),"crossProduct() for parallel vectors does not throw an exception");

    }
}