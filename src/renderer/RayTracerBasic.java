package renderer;

import primitives.*;
import scene.Scene;
import static geometries.Intersectable.GeoPoint;

import java.util.List;

/**
 * class of ray tracer basic that inherit from ray tracer base
 */
public class RayTracerBasic extends RayTracerBase {
    /**
     * constructor
     * @param  scene of this case
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * find the color of the closer intersection point in the scene
     * @param ray from the camera to the intersection points
     * @return
     */
    @Override
    public Color traceRay(Ray ray) {
        //list of all intersection points
        List<GeoPoint> intersections = _scene.getGeometries().findGeoIntersections(ray);
        //there are intersection points
        if (intersections != null) {
            //find the closer intersection point
            GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
            //return its color
            return calcColor(closestPoint);
        }
        //ray did not intersect any geometrical object
        return _scene.getBackground();
    }

    /**
     * find the color by point
     * @param intersection of intersection
     * @return its color
     */
    private Color calcColor(GeoPoint intersection) {
        return _scene.getAmbientLight().getIntensity().add(intersection._geometry.getEmission());
    }

}
