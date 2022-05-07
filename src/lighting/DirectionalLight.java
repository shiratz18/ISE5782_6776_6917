package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;
/**
 * class for a direction light without position
 */
public class DirectionalLight extends Light implements LightSource{
    private Vector _direction;
    /**
     *constructor of direction light
     * @param intensity=the color of the light
     * @param direction=the direction of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        _direction = direction.normalize();
    }
    /**
     * getter for intensity
     * @param p= the point that we return its color
     * @return the intensity color
     */
    @Override
    public Color getIntensity(Point p) {return getIntensity();}
    /**
     * getter for intensity
     * @param p= the point that we return its color
     * @return the intensity color
     */
    @Override
    public Vector getL(Point p) {return _direction;}


    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}
