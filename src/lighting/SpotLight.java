package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Util;
import primitives.Vector;
/**
 * class for a spotLight - light with direction and position

 */
public class SpotLight extends PointLight{
    private Vector _direction;

    public SpotLight(Color intensity, Point position, double kC, double kL, double kQ, Vector direction) {
        super(intensity, position, kC, kL, kQ);
        _direction = direction;
    }
    /**
     * constructor of spotLight
     * @param intensity=the color of the light
     * @param position=the position of the light
     * @param direction=the direction of the light
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        _direction = direction.normalize();
    }
    /**
     * getter for intensity
     * @param p=the point that we return its color
     * @return the intensity color
     */
    @Override
    public Color getIntensity(Point p) {
        Color pointIntensity = super.getIntensity(p);
        Vector l=getL(p);
        double attenuation=l.dotProduct(_direction);
        Color result = pointIntensity.scale(Math.max(0,attenuation));
        return (result);
    }
    //bonus
    public SpotLight setNarrowBeam(int i) {
        return this;
    }
}
