package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable {
    List<Intersectable> _intersectables = null;

    /**
     * constructor
     * @param intersectables all the intersections of geometries
     */
    public Geometries(Intersectable... intersectables) {
        _intersectables = new LinkedList<>();
        add(intersectables);
    }

    /**
     * adding intersections to the collection
     * @param intersectables all the intersections of geometries
     */
    public void add(Intersectable... intersectables) {
        Collections.addAll(_intersectables, intersectables);
    }

    /**
     * default construction
     */
    public Geometries() {
        _intersectables = new LinkedList<>();
    }
    /**
     *finding all intersection points by checking every case
     * @param ray the ray {@link Ray} that intersect with the graphic object
     * @return list of intersection points
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> intersections = null;
        for (Intersectable item : _intersectables) {
            //get intersections points of a particular item from _intersectables
            List<GeoPoint> itempoints = item.findGeoIntersections(ray,maxDistance);
            if(itempoints!= null){
                //first time initialize result to new LinkedList
                if(intersections== null){
                    intersections= new LinkedList<>();
                }
                //add all item points to the resulting list
                intersections.addAll(itempoints);
            }
        }
        return intersections;
    }


}
