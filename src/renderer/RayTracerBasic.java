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
    private static final Double3 INITIAL_K = new Double3(1.0d);
    private static final double MIN_CALC_COLOR_K = 0.001;
    //private static  final double DELTA = 0.1;

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
        //find the closer intersection point
        GeoPoint closestPoint = findClosestIntersection(ray);
        //ray did not intersect any geometrical object
        if(closestPoint==null) {
          return _scene.getBackground();
        }
        //return its color
        return calcColor(closestPoint,ray);
    }


    /**
     * find the color by point
     * @param intersection of intersection
     * @return its color
     */
    private Color calcColor(GeoPoint intersection, Ray ray) {
        return calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
               .add(_scene.getAmbientLight().getIntensity());

  }
    /**
     * calculate the color at a specific point
     * @param intersection the point that we calculate its color
     * @param ray the ray towards the pixel
     * @param level the level of the recursion
     * @return the color at this point with the local and global effects
     */
    private Color calcColor(GeoPoint intersection, Ray ray, int level, Double3 k) {
        Color color = intersection._geometry.getEmission()
                .add(calcLocalEffects(intersection, ray, k));
        if(level == 1)
            return color;
        color = color.add(calcGlobalEffects(intersection, ray, level, k));
        return color;
    }
    /**
     *calculate the global effects on the color in a point
     * @param gp the closet point intersect with the ray
     * @param ray the raya from the camera
     * @param level the level of the recursion
     * @return the global effects color
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;
        Vector n = gp._geometry.getNormal(gp._point);
        Material material = gp._geometry.getMaterial();
        Double3 kr = material._kr, kkr = k.product(kr);
        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) {
            Ray reflectedRay = constructReflectedRay(gp._point, ray, n);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
            if (reflectedPoint != null)
                color = color.add(calcColor(reflectedPoint, reflectedRay, level-1, kkr).scale(kr));
        }
        Double3 kt = material._kt , kkt = k.product(kt);
        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) {
            Ray refractedRay = constructRefractedRay(gp._point, ray, n);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (refractedPoint != null)
                color = color.add(calcColor(refractedPoint, refractedRay, level- 1, kkt).scale(kt));
        }
        return color;
    }


    private Color calcGlobalEffect(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = findClosestIntersection (ray);
        return (gp == null ? _scene.getBackground() : calcColor(gp, ray, level - 1, kkx)
        ).scale(kx);
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
    private Color calcLocalEffects(GeoPoint intersection, Ray ray, Double3 k) {
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
                Double3 ktr= transparency(intersection,lightSource,l,n);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)){
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
       // Vector epsVector = n.scale(nl < 0 ? DELTA : -1*DELTA);
       // Point point = gp._point.add(epsVector);
        Ray lightRay = new Ray(gp._point, lightDirection, n);
        List<GeoPoint> intersections = _scene.getGeometries().findGeoIntersections(lightRay,light.getDistance(gp._point));
        if(intersections==null)
            return true;
        for (GeoPoint geoP : intersections)
            if(geoP._geometry.getMaterial()._kt.equals(new Double3(0.0)))
                return false;
        return true;
    }
    /**
     * calculate how shaded the point is
     * @param gp the geo point we check how shaded it is
     * @param light the light source
     * @param l direction from light to point
     * @param n normal from the object at the point
     * @return the shadow level on the spot
     */
    private Double3 transparency(GeoPoint gp, LightSource light, Vector l, Vector n){
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(gp._point, lightDirection, n);
        List<GeoPoint> intersections = _scene.getGeometries()
                .findGeoIntersections(lightRay, light.getDistance(gp._point));
        Double3 ktr= new Double3(1.0);
        if(intersections==null)
            return ktr;

        for (GeoPoint geoP : intersections) {
            ktr = ktr.product(geoP._geometry.getMaterial()._kt);
            if(ktr.lowerThan(MIN_CALC_COLOR_K))
                return new Double3(0.0);
        }
        return ktr;
    }

    /**
     * calculate the reflected ray with shift in delta
     * @param p the initial point
     * @param ray the ray towards the object
     * @param n the normal
     * @return the reflected ray
     */
    private Ray constructRefractedRay(Point p, Ray ray, Vector n) {
        //r = v - 2.(v.n).n
        Vector v = ray.getDir();
        double vn = v.dotProduct(n);

        if (vn == 0) {
            return null;
        }

        Vector r = v.subtract(n.scale(2 * vn));
        return new Ray(p, r, n);

    }
    /**
     * calculate the refracted ray with shift in delta
     * @param p the initial point
     * @param ray the ray towards the object
     * @param n the normal
     * @return the refracted ray
     */
    private Ray constructReflectedRay(Point p, Ray ray, Vector n) {
        return new Ray(p, ray.getDir(), n);
    }

}

