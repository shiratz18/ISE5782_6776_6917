package geometries;
import primitives.*;
public class Cylinder extends Tube{
    private  double _height;

    /**
     * constructor
     * @param axisRay ray
     * @param radius
     * @param height
     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        _height = height;
    }

    /**
     * getting height
     * @return height of cylinder
     */
    public double getHeight() {
        return _height;
    }

    /**
     * to string
     * @return values of cylinder
     */
    @Override
    public String toString() {
        return super.toString()+ "Cylinder{" +
                "_height=" + _height+
                '}';
    }

    /**
     * calculating the normal of cylinder
     * @param point should be null for flat geometries
     * @return vector of this normal
     */
    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
