package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Tests for Vector
 *
 * @author Shirel
 */
class VectorTest {
    Vector v1= new Vector(1,2,3);
    Vector v2= new Vector(-2,-4,-6);

    @Test
    void add() {
    }

    @Test
    void scale() {
    }

    @Test
    void dotProduct() {


     assertEquals(-28, v1.dotProduct(v2));
    }

    @Test
    void lengthSquared() {
    }

    @Test
    void length() {
    }

    @Test
    void normalize() {
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