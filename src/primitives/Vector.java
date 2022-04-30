package primitives;

/**
 * This class presents a vector by a point in space
 */
public class Vector extends Point {
    /**
     * constructor
     * @param x coordinate value for X axis
     * @param y coordinate value for Y axis
     * @param z coordinate value for Z axis
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (Double3.ZERO.equals(_xyz)) {
            throw new IllegalArgumentException("ZERO vector not allowed");
        }
    }

    /**
     * constructor
     * @param d value of double3
     */
    public Vector(Double3 d) {
        super(d);
    }
    public Vector(Vector v) {
        this(v._xyz);
    }
    /**
     * equalsing between two objects
     * @param o Object (basicaly another Point3d) to compare
     * @return true or false accordingly
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * to string
     * @return value of vector
     */
    @Override
    public String toString() {
        return "Vector{" +
                "xyz=" + _xyz +
                '}';
    }

    /**
     * the function calculates plus between vectors
     * @param other the vector to add
     * @return algebraic added vector
     */
    public Vector add(Vector other) {
        //create a new double 3 of the result of adding between 2 vectors
        Double3 result = this._xyz.add(other._xyz);
        //if the vectors are the same but in the opposite
        if (Double3.ZERO.equals(result)) {
            throw new IllegalArgumentException("add vector resulting in ZERO vector not allowed");
        }
        return new Vector(result._d1,result._d2, result._d3);
    }

    /**
     * Scale (multiply) floating point triad by a number into a new triad where each
     * number is multiplied by the number
     * @param s right handle side operand for scaling
     * @return result of scale in vector
     */
     public Vector scale(double s)
     {
         return new Vector(this._xyz.scale(s));
     }

    /**
     * dot product between two vectors (scalar product)
     * @param vector the right vector of U.V
     * @return scale value of dot product
     */
     public double dotProduct(Vector vector)
     {
         //create a new double 3 of multi between 2 vectors
         Double3 newD =_xyz.product(vector._xyz);
         //return the sum of the coordinate result
         return newD._d1+newD._d2+ newD._d3;
     }

    /**
     * calculating the length of vector
     * @return euclidean length squared of the vector
     */
     public double lengthSquared()
     {
         double u1 =_xyz._d1;
         double u2 = _xyz._d2;
         double u3 = _xyz._d3;

         return u1 * u1 + u2 * u2 + u3 * u3;
     }

    /**
     *calculating the length of vector by Pythagoras
     * @return the length of this vector
     */
    public double length()
    {
        return Math.sqrt(lengthSquared());
    }

    /**
     * calculate the normal of this vector
     * @return normalize vector
     */
     public Vector normalize()
     {
         double len =this.length();
         return new Vector(this._xyz._d1/len,this._xyz._d2/len ,this._xyz._d3/len );
     }

    /**
     * Cross product (vectorial product)
     * @param v second vector
     * @return new vector resulting from cross product
     */
    public Vector crossProduct(Vector v) {
        double u1 = _xyz._d1;
        double u2 = _xyz._d2;
        double u3 = _xyz._d3;
        double v1 = v._xyz._d1;
        double v2 = v._xyz._d2;
        double v3 = v._xyz._d3;

        return new Vector(
                u2 * v3 - u3 * v2,
                u3 * v1 - u1 * v3,
                u1 * v2 - u2 * v1
        );
    }
}
