package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Double3Test {

    Double3 d1 = new Double3(2, 4, 6);
    Double3 d2 = new Double3(1, 1, 2);

    /**
     * Test method for {@link primitives.Double3#add(Double3 rhs)}.
     */
    @Test
    void testAdd() {
        //Simple test of adding
        assertEquals(new Double3(3, 5, 8), d1.add(d2), "Wrong double3 add");
    }

    @Test
    void testSubtract() {
        //  Simple test
        assertEquals(
                new Double3(1, 3, 4), d1.subtract(d2), "Wrong double3 subtract");
    }

    @Test
    void testScale() {
        //Simple test of vector scale
        assertEquals(new Double3(4, 8, 12), d1.scale(2), "Wrong double3 scale");
    }

    @Test
    void testReduce() {
        // ============ Equivalence Partitions Tests ==============
        //Simple test of reducing
        assertEquals(new Double3(1, 2, 3), d1.reduce(2), "Wrong double3 reduce");

    }

    @Test
    void product() {
        //Simple test of product
        assertEquals(new Double3(2, 4, 12), d1.product(d2), "Wrong double3 product");
    }
}

