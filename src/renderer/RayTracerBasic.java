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
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double INITIAL_K = 1d;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static  final double DELTA = 0.1;

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

    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        Color ambientLight = _scene.getAmbientLight().getIntensity();
        Color emissionLight = geoPoint._geometry.getEmission();
        Color localEffects = calcLocalEffect(geoPoint,ray);
        return ambientLight.add(emissionLight).add(localEffects);
    }
    /**
     * find the color by point
     * @param intersection of intersection
     * @return its color
     */
 //   private Color calcColor(GeoPoint intersection, Ray ray) {
       // Color ambientLight = _scene.getAmbientLight().getIntensity();
//        Color Il = intersection._geometry.getEmission();
//
//        Color result = Il.add(calcLocalEffect(intersection,ray), ambientLight);
      //  return calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
         //       .add(ambientLight);

 //   }
/**
    private Color calcColor(GeoPoint intersection, Ray ray, int level, double k) {
        //aliba de shaili
        Color color = calcLocalEffect(intersection, ray);
        if(level == 1)
            return color;
        color = color.add(calcGlobalEffects(intersection, ray, level, k));
        return color;
    }

    private Color calcGlobalEffects(GeoPoint gp, Vector v, int level, double k) {
        Color color = Color.BLACK; Vector n = gp._geometry.getNormal(gp._point);
        Material material = gp._geometry.getMaterial();
        Double3 kkr = material._kr.scale(k);
        if (! kkr.lowerThan(MIN_CALC_COLOR_K))
            color = calcGlobalEffect(constructReflectedRay(gp._point, v, n), level, material._kr, kkr);
        Double3 kkt = material._kt.scale(k);
        if (! kkt.lowerThan(MIN_CALC_COLOR_K))
            color = color.add(
                    calcGlobalEffect(constructRefractedRay(gp._point, v, n), level, material._kt, kkt));
        return color;
    }

    private Ray constructRefractedRay(Point point, Vector v, Vector n) {

    }

    private Ray constructReflectedRay(Point point, Vector v, Vector n) {
        Point deltaPoint = point.add(n.scale(DELTA));
        return new Ray(deltaPoint,v);
    }

    private Color calcGlobalEffect(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = findClosestIntersection (ray);
        return (gp == null ? _scene.getBackground() : calcColor(gp, ray, level - 1, kkx)
        ).scale(kx));
    }
    /*
    /**
     * Scans the ray and looks for the first point that cuts the ray
     * @param ray the ray
     * @return the closest point that cuts the ray and null if there is no points
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = _scene.getGeometries().findGeoIntersections(ray);
        return intersections == null? null :  ray.findClosestGeoPoint(intersections);
    }
    /**
     * calculated light contribution from all light sources
     * @param intersection the geo point we calculate the color of
     * @param ray ray from the camera to the point
     * @return the color from the lights at the point
     */
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
                if (unshaded(lightSource, intersection, l, n, nl)) {
                    Color lightIntensity = lightSource.getIntensity(intersection._point);
                    color = color.add(calcDiffusive(kd, nl, lightIntensity),
                            calcSpecular(ks, l, n, nl, v, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }
    /**
     * Calculate the Specular component of the light at this point
     * @param ks specular component
     * @param l direction from light to point
     * @param n normal from the object at the point
     * @param nl dot-product n*l
     * @param V direction from the camera to the point
     * @param nShininess shininess level
     * @param ip light intensity
     * @return the Specular component at the point
     */
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
    /**
     * Calculate the diffusive component of the light at this point
     * @param kd diffusive component
     * @param nl dot-product n*l
     * @param ip light intensity
     * @return the diffusive component at the point
     */
    private Color calcDiffusive(Double3 kd, double nl, Color ip) {
        return ip.scale(kd.scale(Math.abs(nl)));
    }

    /**
     * Checking for shading between a point and the light source
     * @param light the light source
     * @param gp the peo point which is shaded or not
     * @param l direction from light to point
     * @param n normal from the object at the point
     * @param nl dot-product n*l
     * @return
     */
    private boolean unshaded(LightSource light, GeoPoint gp, Vector l, Vector n, double nl){
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector epsVector = n.scale(nl < 0 ? DELTA : -1*DELTA);
        Point point = gp._point.add(epsVector);
        Ray lightRay = new Ray(point, lightDirection);
        List<GeoPoint> intersections = _scene.getGeometries().findGeoIntersections(lightRay,light.getDistance(gp._point));
        return intersections==null;
    }
}
