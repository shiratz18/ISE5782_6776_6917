package geometries;

import lighting.AmbientLight;
import primitives.*;
import scene.Scene;

/**
 * interface for all graphic geometry that have a normal
 */
public abstract class Geometry extends Intersectable {
    private Color _emission=Color.BLACK;
    private Material _material=new Material();

    public Geometry setMaterial(Material material) {
        _material = material;
        return this;
    }

    public Material getMaterial() {
        return _material;
    }

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

