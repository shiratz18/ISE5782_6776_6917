package geometries;

import lighting.AmbientLight;
import primitives.Color;
import primitives.Double3;
import  primitives.Vector;
import  primitives.Point;
import scene.Scene;

/**
 * interface for all graphic geometry that have a normal
 */
public abstract class Geometry extends Intersectable {
    protected Color _emission=Color.BLACK;
  
    /**
     * getter emission
     * @return emission
     */
    public Color getEmission() {
        return _emission;
    }

    /**
     * setter emission
     * @param emission color of geometry
     * @return the emission
     */
    public Geometry setEmission(Color emission) {
        _emission = emission;
        return this;
    }
    /**
     *
     * @param point point to drive the normal to
     * @return normal Vector {@link Vector}
     */
    public abstract Vector getNormal(Point point);


}

