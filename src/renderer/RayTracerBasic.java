package renderer;

import primitives.*;
import scene.Scene;

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
        List<Point> intersections = _scene.getGeometries().findIntersections(ray);
        //there are intersection points
        if (intersections != null) {
            //find the closer intersection point
            Point closestPoint = ray.findClosestPoint(intersections);
            //return its color
            return calcColor(closestPoint);
        }
        //ray did not intersect any geometrical object
        return _scene.getBackground();
    }

    /**
     * find the color by point
     * @param point of intersection
     * @return its color
     */
    private Color calcColor(Point point) {
        return _scene.getAmbientLight().getIntensity();
    }

}
