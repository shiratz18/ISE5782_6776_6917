package geometries;

import java.util.LinkedList;
import java.util.List;
import geometries.*;
import primitives.*;
import static primitives.Util.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 */
public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected List<Point> _vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected Plane _plane;
    private int _size;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this._vertices = List.of(vertices);
        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        _plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (vertices.length == 3)
            return; // no need for more tests for a Triangle

        Vector n = _plane.getNormal();

        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with
        // the normal. If all the rest consequent edges will generate the same sign -
        // the
        // polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
        _size = vertices.length;
    }

    /**
     * calculating the normal of Polygon
     * @param point point to drive the normal to
     * @return
     */
    @Override
    public Vector getNormal(Point point) {
        return _plane.getNormal();
    }

    /**
     *finding all intersection points by checking every case
     * @param ray the ray {@link Ray} that intersect with the graphic object
     * @return list of intersection points
     */
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {

        List<GeoPoint> result = _plane.findGeoIntersectionsHelper(ray, maxDistance);
        if (result == null) {
            return null;
        }
        int numV = _vertices.size();
        Point p0 = ray.getP0();
        Vector dir = ray.getDir();

        Vector vector1 = _vertices.get(numV - 1).subtract(p0);
        Vector vector2 = _vertices.get(0).subtract(p0);

        Vector normal = vector1.crossProduct(vector2).normalize();
        double dirDotProN = dir.dotProduct(normal);
        boolean positive = dirDotProN > 0;
        if (isZero(dirDotProN)) {
            return null;
        }

        for (int i = 1; i < numV; ++i) {
            vector1 = vector2;
            vector2 = _vertices.get(i).subtract(p0);
            normal = vector1.crossProduct(vector2).normalize();
            dirDotProN = dir.dotProduct(normal);

            if (isZero(dirDotProN) || dirDotProN > 0 != positive) {
                return null;
            }
        }
        return List.of(new GeoPoint(this, result.get(0)._point));
    }
}
