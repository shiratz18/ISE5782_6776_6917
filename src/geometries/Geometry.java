package geometries;

import primitives.Double3;
import  primitives.Vector;
import  primitives.Point;

/**
 * interface for all graphic geometry that have a normal
 */
public interface Geometry extends Intersectable {
    /**
     *
     * @param point point to drive the normal to
     * @return normal Vector {@link Vector}
     */
    public Vector getNormal(Point point);
}

