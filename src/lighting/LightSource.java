package lighting;

import primitives.*;

/**
 * interface for common actions of light sources
 */
public interface LightSource {
    /**
     * Get light source intensity as it reaches a point IP
     * @param p the illuminated point
     * @return intensity IP
     */
    public Color getIntensity(Point p);

    /**
     * Get normalized vector in the direction from light source towards the lighted point
     * @param p – the lighted point
     * @return light to point vector
     */
    public Vector getL(Point p);

    /**
     * calculate the distance between the point to the light source
     * @param point the point
     * @return the distance from point to the light
     */
    double getDistance(Point point);

}
