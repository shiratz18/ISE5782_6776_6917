package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;
import static geometries.Intersectable.GeoPoint;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;
import static java.lang.Math.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * class of ray tracer basic that inherit from ray tracer base
 */
public class RayTracerBasic extends RayTracerBase {

    private static final double EPS = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = new Double3(1.0d);
    private int _glossinessRays = 10;
    private double _distanceGrid = 25;
    private double _sizeGrid=4;

    /**
     * setter of glossinessRays
     * @param glossinessRays
     * @return ray of TracerBasic
     */
    public RayTracerBasic setGlossinessRays(int glossinessRays) {
        if (glossinessRays <= 0) {
            throw new IllegalArgumentException("number of glossiness rays should be greater than 0");
        }
        _glossinessRays = glossinessRays;
        return this;
    }
    /**
     * constructor
     * @param  scene of this case
     */
    public RayTracerBasic(Scene scene) {super(scene);}

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

    @Override
    public Color averageColor(LinkedList<Ray> rays) {
        Color color=Color.BLACK;
        for( Ray ray:rays){
            color=color.add(traceRay(ray));
        }
        return color.reduce(Double.valueOf(rays.size()));
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
     * @param geoPoint the point that we calculate its color
     * @param ray the ray towards the pixel
     * @param level the level of the recursion
     * @return the color at this point with the local and global effects
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = geoPoint._geometry.getEmission();

        Vector v = ray.getDir();
        Vector n = geoPoint._geometry.getNormal(geoPoint._point);

        // check that ray is not parallel to geometry
        double nv = alignZero(n.dotProduct(v));

        if (isZero(nv)) {
            return color;
        }
        Material material = geoPoint._geometry.getMaterial();
        color = color.add(calcLocalEffects(geoPoint, material, n, v, nv, k));
        return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, material,n,v,nv, level, k));
    }
    /**
     * calculate the global effects on the color in a point
     * @param gp the closet point intersect with the ray
     * @param material
     * @param n
     * @param v
     * @param nv
     * @param level the level of the recursion
     * @param k
     * @return the global effects color
*/
    private Color calcGlobalEffects(GeoPoint gp,Material material, Vector n, Vector v, double nv, int level, Double3 k) {
        Color color = Color.BLACK;
        //adds the reflection effect
        Double3 kkr = material.getKr().product(k);
        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) {
            Ray[] reflectedRays = constructReflectedRays(gp._point, v, n, material.getkG(), _glossinessRays);
            for (Ray reflectedRay : reflectedRays) {
                color = color.add(calcGlobalEffect(reflectedRay, level, material.getKr(), kkr)
                        .scale(1d / reflectedRays.length));
            }
        }
        //adds the refraction effect
        Double3 kkt = material.getKt().product(k);
        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) {
            Ray[] refractedRays = constructRefractedRays(gp._point, v, n.scale(-1), material.getkG(), _glossinessRays);
            for (Ray refractedRay : refractedRays) {
                color = color.add(calcGlobalEffect(refractedRay, level, material.getKt(), kkt)
                        .scale(1d / refractedRays.length));
            }
        }
            return color;
    }

    /**
    * @param nv
     * @param level the level of the recursion
     * @param k
     * @return the global effects color

    private Color calcGlobalEffects(GeoPoint gp,Material material, Vector n, Vector v, double nv, int level, Double3 k) {
        Color color = Color.BLACK;
        Double3 kkr = material.getKr().product(k);
        if (!kkr.lowerThan(MIN_CALC_COLOR_K))
            color = color.add(calcGlobalEffect(constructReflectedRay(gp._point, v, n), level, material.getKr(), kkr));
        Double3 kkt = material.getKt().product(k);
        if (!kkt.lowerThan(MIN_CALC_COLOR_K))
            color = color.add(
                    calcGlobalEffect(constructRefractedRay(gp._point, v, n), level, material.getKt(), kkt));
        return color;
    }
*/
    /**
     *help function to the recursion
     * @param ray from the geometry
     * @param level of recursion
     * @param kx parameter of the recursion
     * @param kkx parameter of the recursion
     * @return the calculating color
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = findClosestIntersection (ray);
        return (gp == null ? _scene.getBackground() : calcColor(gp, ray, level - 1, kkx)).scale(kx);
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
     * //add here the lights effects
     *
     * @param gp  geopoint of the intersection
     * @param v ray direction
     * @return resulting color with diffuse and specular
     */
    private Color calcLocalEffects(GeoPoint gp, Material material, Vector n, Vector v, double nv, Double3 k) {
        Color color = Color.BLACK;

        Point point = gp._point;

        for (LightSource lightSource : _scene.getLights()) {
            Vector l = lightSource.getL(point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sign(nv)
                Double3 ktr = transparency(lightSource, l, n, gp);
                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                //if (unshaded(gp, lightSource, l, n,nv)) {
                    Color iL = lightSource.getIntensity(point).scale(ktr);
                    color = color.add(
                            calcDiffusive(material.getkD(), nl,iL),
                            calcSpecular(material.getkS(), n, l, nl, v,material.getnShininess(),iL));
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
    private Color calcSpecular(Double3 ks, Vector n, Vector l, double nl, Vector V, int nShininess, Color ip) {
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
        double abs_nl = Math.abs(nl);
        Double3 amount =kd.scale(abs_nl);
        return ip.scale(amount);
    }

    /**
     * Checking for shading between a point and the light source
     *  @param gp the peo point which is shaded or not
     * @param lightSource the light source
     * @param l direction from light to point
     * @param n normal from the object at the point
     * @param nv dot-product n*l
     * @return
     */

    private boolean unshaded(GeoPoint gp, LightSource lightSource, Vector l, Vector n, double nv) {

        Vector lightDirection = l.scale(-1); // from point to light source
        double nl = n.dotProduct(lightDirection);

        Vector delta = n.scale(nl > 0 ? EPS : -EPS);
        Point pointRay = gp._point.add(delta);
        Ray lightRay = new Ray(pointRay, lightDirection);

        double maxdistance = lightSource.getDistance(gp._point);
        List<GeoPoint> intersections = _scene.getGeometries().findGeoIntersections(lightRay, maxdistance);

        return intersections == null;
    }

    /**
     * calculate how shaded the point is
     * @param gp the geo point we check how shaded it is
     * @param light the light source
     * @param l direction from light to point
     * @param n normal from the object at the point
     * @return the shadow level on the spot
     */
    private Double3 transparency( LightSource light, Vector l, Vector n, GeoPoint gp){
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(gp._point, lightDirection, n);
        List<GeoPoint> intersections = _scene.getGeometries()
                .findGeoIntersections(lightRay, light.getDistance(gp._point));
        Double3 ktr= new Double3(1.0);
        if(intersections==null)
            return ktr;

        for (GeoPoint geoP : intersections) {
            ktr = ktr.product(geoP._geometry.getMaterial().getKt());
            if(ktr.lowerThan(MIN_CALC_COLOR_K))
                return new Double3(0.0);
        }
        return ktr;
    }

    /**
     * calculate the reflected ray with shift in delta
     * @param p the initial point
     * @param v the vector towards the object
     * @param n the normal
     * @return the reflected ray
     */
    private Ray constructRefractedRay(Point p, Vector v, Vector n) {return new Ray(p,v,n);}

    /**
     * Constructs randomized reflection rays at the intersection point according to kG.
     * If kG is 1 then only one ray is returned with the specular vector
     *
     * @param point the intersection point
     * @param v     the intersection's ray direction
     * @param n     the normal at the intersection point
     * @param kG    the glossiness parameter in range of [0,1], where 0 - matte, 1 - glossy
     * @return randomized reflection rays
     */
    private Ray[] constructReflectedRays(Point point, Vector v, Vector n, double kG, int numOfRays) {
        Vector n2vn = n.scale(-2 * v.dotProduct(n));
        Vector r = v.add(n2vn);

        // If kG is equals to 1 then return only 1 ray, the specular ray (r)
        if (isZero(kG - 1)) {
            return new Ray[]{new Ray(point, r, n)};
        }

        Vector[] randomizedVectors = createRandomVectors(n, numOfRays);

        // If kG is equals to 0 then select all the randomized vectors
        if (isZero(kG)) {
            return Arrays.stream(randomizedVectors)
                    .map(vector -> new Ray(point, vector, n))
                    .toArray(Ray[]::new);
        }

        // If kG is in range (0,1) then move the randomized vectors towards the specular vector (v)
        return Arrays.stream(randomizedVectors)
                .map(vector -> new Ray(point,
                        vector.scale(1-kG).add(r.scale(kG)), n))
                .toArray(Ray[]::new);
    }


    /**
     * Constructs randomized refraction rays at the intersection point according to kG.
     * If kG is 1 then only one ray is returned with the vector v (which is the specular vector).
     *
     * @param point the intersection point
     * @param v     the intersection's ray direction
     * @param n     the normal at the intersection point
     * @param kG    the glossiness parameter in range of [0,1], where 0 - matte, 1 - glossy
     * @return randomized refraction rays
     */
    private Ray[] constructRefractedRays(Point point, Vector v, Vector n, double kG, int numOfRays) {
        // If kG is equals to 1 then return only 1 ray, the specular ray (v)
        if (isZero(kG - 1)) {
            return new Ray[]{new Ray(point, v, n)};
        }

        Vector[] randomizedVectors = createRandomVectors(n, numOfRays);

        // If kG is equals to 0 then select all the randomized vectors
        if (isZero(kG)) {
            return Arrays.stream(randomizedVectors)
                    .map(vector -> new Ray(point, vector, n))
                    .toArray(Ray[]::new);
        }

        // If kG is in range (0,1) then move the randomized vectors towards the specular vector (v)
        return Arrays.stream(randomizedVectors)
                .map(vector -> new Ray(point,
                        vector.scale(1 - kG).add(v.scale(kG)), n))
                .toArray(Ray[]::new);
    }


    /**
     * calculate the refracted ray with shift in delta
     * @param pointGeo the initial point
     * @param v the vector towards the object
     * @param n the normal
     * @return the refracted ray
     */
    private Ray constructReflectedRay(Point pointGeo, Vector v, Vector n) {
        //r = v - 2.(v.n).n
        double vn = v.dotProduct(n);

        if (vn == 0) {
            return null;
        }

        Vector r = v.subtract(n.scale(2 * vn));
        return new Ray(pointGeo, r, n);
    }
    /**
     * Creates random vectors on the unit hemisphere with a given normal on the hemisphere's bottom.<br>
     * source: https://my.eng.utah.edu/~cs6958/slides/pathtrace.pdf#page=18
     *
     * @param n normal to the hemisphere's bottom
     * @return the randomized vectors
     */
    private Vector[] createRandomVectors(Vector n, int numOfVectors) {
        // pick axis with smallest component in normal
        // in order to prevent picking an axis parallel
        // to the normal and eventually creating zero vector
        Vector axis;
        if (Math.abs(n.getX()) < Math.abs(n.getY()) && Math.abs(n.getX()) < Math.abs(n.getZ())) {
            axis = new Vector(1, 0, 0);
        } else if (Math.abs(n.getY()) < Math.abs(n.getZ())) {
            axis = new Vector(0, 1, 0);
        } else {
            axis = new Vector(0, 0, 1);
        }

        // find two vectors orthogonal to the normal
        Vector x = n.crossProduct(axis);
        Vector z = n.crossProduct(x);

        Vector[] randomVectors = new Vector[numOfVectors];
        for (int i = 0; i < numOfVectors; i++) {
            // pick a point on the hemisphere bottom
            double u, v, u2, v2;
            do {
                u = random() * 2 - 1;
                v = random() * 2 - 1;
                u2 = u * u;
                v2 = v * v;
            } while (u2 + v2 >= 1);

            // calculate the height of the point
            double w = Math.sqrt(1 - u2 - v2);

            // create the new vector according to the base (x, n, z) and the coordinates (u, w, v)
            randomVectors[i] = x.scale(u)
                    .add(z.scale(v))
                    .add(n.scale(w));
        }

        return randomVectors;
    }
    public Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight, Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints) {
        if (Width < minWidth * 2 || Height < minHeight * 2) {
            return this.traceRay(new Ray(cameraLoc, centerP.subtract(cameraLoc))) ;
        }

        List<Point> nextCenterPList = new LinkedList<>();
        List<Point> cornersList = new LinkedList<>();
        List<primitives.Color> colorList = new LinkedList<>();
        Point tempCorner;
        Ray tempRay;
        for (int i = -1; i <= 1; i += 2){
            for (int j = -1; j <= 1; j += 2) {
                tempCorner = centerP.add(Vright.scale(i * Width / 2)).add(Vup.scale(j * Height / 2));
                cornersList.add(tempCorner);
                if (prePoints == null || !isInList(prePoints, tempCorner)) {
                    tempRay = new Ray(cameraLoc, tempCorner.subtract(cameraLoc));
                    nextCenterPList.add(centerP.add(Vright.scale(i * Width / 4)).add(Vup.scale(j * Height / 4)));
                    colorList.add(traceRay(tempRay));
                }
            }
        }


        if (nextCenterPList == null || nextCenterPList.size() == 0) {
            return primitives.Color.BLACK;
        }


        boolean isAllEquals = true;
        primitives.Color tempColor = colorList.get(0);
        for (primitives.Color color : colorList) {
            if (!tempColor.isAlmostEquals(color))
                isAllEquals = false;
        }
        if (isAllEquals && colorList.size() > 1)
            return tempColor;


        tempColor = primitives.Color.BLACK;
        for (Point center : nextCenterPList) {
            tempColor = tempColor.add(AdaptiveSuperSamplingRec(center, Width/2,  Height/2,  minWidth,  minHeight ,  cameraLoc, Vright, Vup, cornersList));
        }
        return tempColor.reduce(nextCenterPList.size());


    }
    private boolean isInList(List<Point> pointsList, Point point) {
        for (Point tempPoint : pointsList) {
            if(point.equals(tempPoint))
                return true;
        }
        return false;
    }
    public Color traceRay(List<Ray> rays)
    {
        Color finalColor=null;
        Color firstColor=null;
        Color colorTmp=new Color(0,0,0);
        for(var ray:rays)
        {
            List<GeoPoint> intersection = _scene.getGeometries().findGeoIntersections(ray);
            if (intersection == null)
            {
                return _scene.getBackground();
            }
            GeoPoint closestPoint = ray.findClosestGeoPoint(intersection);

            colorTmp=calcColor(closestPoint, ray) == null ? _scene.getBackground() : calcColor(closestPoint, ray);
            if(finalColor==null)
            {
                firstColor=colorTmp;
                finalColor=new Color(0,0,0);
                for (int i = 0; i < 10; i++)
                    finalColor=finalColor.add(colorTmp);
            }

            if(!colorTmp.equals(firstColor))
                finalColor=finalColor.add(colorTmp);

        }
        if(finalColor.equals(firstColor))
            return firstColor;
        int size=rays.size()+10;
        return finalColor.reduce(size);
    }

}

