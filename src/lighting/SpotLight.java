package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Util;
import primitives.Vector;
/**
 * class for a spotLight - light with direction and position

 */
public class SpotLight extends PointLight{
    private final Vector _direction;
    private double _narrowBeam=0d;

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
     *
     * @param narrowBeam
     */
    public SpotLight setNarrowBeam(double narrowBeam) {
        _narrowBeam = narrowBeam;
        return this;
    }
    public double getNarrowBeam() {
        return _narrowBeam;
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
        double factor=Math.max(0,attenuation);
        Color result = pointIntensity.scale(factor);
        return (result);
    }
    //bonus
    //public SpotLight setNarrowBeam(int i) {
    //    return this;
    }

