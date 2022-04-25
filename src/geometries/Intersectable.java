package geometries;

import primitives.*;

import java.util.List;
import java.util.Objects;

/**
 * Common interface for all objects that get intersect by a ray {@link Ray}
 */
public abstract class Intersectable {
    /**
     *
     * @param ray the ray {@link Ray} that intersect with the graphic object
     * @return immutable List of all those Points {@link Point}
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null
                : geoList.stream().map(gp -> gp._point).toList();
    }

    public  List<GeoPoint> findGeoIntersections(Ray ray){return findGeoIntersectionsHelper(ray);}
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

    //
    public static class GeoPoint {
        public Geometry _geometry;
        public Point _point;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeoPoint geoPoint = (GeoPoint) o;
            return Objects.equals(_geometry, geoPoint._geometry) && Objects.equals(_point, geoPoint._point);
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "_geometry=" + _geometry +
                    ", _point=" + _point +
                    '}';
        }

        @Override
        public int hashCode() {
            return Objects.hash(_geometry, _point);

        }

        /**
         * constructor
         * @param geometry
         * @param point
         */
        public GeoPoint(Geometry geometry, Point point) {
            _geometry = geometry;
            _point = point;
        }
    }

}
