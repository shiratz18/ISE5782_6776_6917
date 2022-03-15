package geometries;

import primitives.*;

import java.util.List;

/**
 * Common interface for all objects that get intersect by a ray {@link Ray}
 */
public interface Intersectable {
    /**
     *
     * @param ray the ray {@link Ray} that intersect with the graphic object
     * @return immutable List of all those Points {@link Point}
     */
    List<Point> findIntersections(Ray ray);
}
