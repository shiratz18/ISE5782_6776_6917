package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import static geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.List;

/**
 * class of ray tracer basic that inherit from ray tracer base
 */
public class RayTracerBasic extends RayTracerBase {
    private Double3 kd;

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
            return calcColor(closestPoint,ray);
        }
        //ray did not intersect any geometrical object
        return _scene.getBackground();
    }

    /**
     * find the color by point
     * @param intersection of intersection
     * @return its color
     */
    private Color calcColor(GeoPoint intersection, Ray ray) {
        return _scene.getAmbientLight().getIntensity().add(intersection._geometry.getEmission()).add(calcLocalEffect(intersection,ray));

    }
    private Color calcLocalEffect(GeoPoint intersection, Ray ray) {
        Vector v = ray.getDir();
        Vector n = intersection._geometry.getNormal(intersection._point);

        double nv = alignZero(n.dotProduct(v)); //nv=n*v
        if (isZero(nv)) {
            return Color.BLACK;
        }

        int nShininess = intersection._geometry.getMaterial()._nShininess;
        Double3 kd = intersection._geometry.getMaterial()._kD;
        Double3 ks = intersection._geometry.getMaterial()._kS;
        Color color = Color.BLACK; //base color

        //for each light source in the scene
        for (LightSource lightSource : _scene.getLights()) {
            Vector l = lightSource.getL(intersection._point); //the direction from the light source to the point
            double nl = alignZero(n.dotProduct(l)); //nl=n*l

            //if sign(nl) == sign(nv) (if the light hits the point add it, otherwise don't add this light)
            if (nl * nv > 0) {
                Color lightIntensity = lightSource.getIntensity(intersection._point);
                color = color.add(calcDiffusive(kd, nl, lightIntensity),
                        calcSpecular(ks, l, n,nl, v, nShininess, lightIntensity));
            }
        }
        return color;
    }
    private Color calcSpecular(Double3 ks, Vector l, Vector n, double nl, Vector V, int nShininess, Color ip) {
        if (isZero(nl)) {
            throw new IllegalArgumentException("nl cannot be Zero for scaling the normal vector");
        }
        Vector R = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double VR = alignZero(V.dotProduct(R));
        if (VR >= 0) {
            return Color.BLACK; // view from direction opposite to r vector
        }
        // [rs,gs,bs]ks(-V.R)^p
        return ip.scale(ks.scale(Math.pow(-1d * VR, nShininess)) );
    }
    private Color calcDiffusive(Double3 kd, double nl, Color ip) {
        return ip.scale(kd.scale(Math.abs(nl)));
    }


}
