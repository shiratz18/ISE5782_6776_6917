package lighting;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;

/**
 * class for a point light with position and without direction
 */
public class PointLight extends Light implements LightSource {
    private final Point _position;
    private Double3 _kC = new Double3(1);// Constant attenuation
    private Double3 _kL = new Double3(0);// Linear attenuation
    private Double3 _kQ = new Double3(0);// Quadratic attenuation


    /**
     * default constructor the constant attenuation value is 1 and the other two values are 0
     *
     * @param intensity
     * @param position
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        _position = position;
    }
    /**
     * setter for kc
     *
     * @param kC the constant attenuation
     * @return the point light
     */
    public PointLight setKc(Double3 kC) {
        _kC = kC;
        return this;
    }
    /**
     * setter for kl
     *
     * @param kL the linear attenuation
     * @return the point light
     */
    public PointLight setKl(Double3 kL) {
        _kL = kL;
        return this;
    }
    /**
     * setter for kq
     *
     * @param kQ the quadratic attenuation
     * @return the point light
     */
    public PointLight setKq(Double3 kQ) {
        _kQ = kQ;
        return this;
    }
    /**
     * setter for kc
     *
     * @param kC the constant attenuation
     * @return the point light
     */
    public PointLight setKc(double kC) {
        _kC = new Double3(kC);
        return this;
    }
    /**
     * setter for kl
     *
     * @param kL the linear attenuation
     * @return the point light
     */
    public PointLight setKl(double kL) {
        _kL = new Double3(kL);
        return this;
    }
    /**
     * setter for kq
     *
     * @param kQ the quadratic attenuation
     * @return the point light
     */
    public PointLight setKq(double kQ) {
        _kQ = new Double3(kQ);
        return this;
    }


    /**
     * getter for intensity
     *
     * @param p=the point that we return its color
     * @return the intensity color
     */
    @Override
    public Color getIntensity(Point p) {
        // attenuate intensity by factor;
        Color lightIntensity =getIntensity();

        double d = p.distance(_position);
        double dsquared = p.distanceSquared(_position);

        //factor of attenuation depends on distance
        Double3 factor = (_kC.add(_kL.scale(d)).add(_kQ.scale(dsquared)));
        return lightIntensity.reduce(factor);
    }

    /**
     * getter for the light direction
     *
     * @param p=the point the light comes to
     * @return the light direction
     */
    @Override
    public Vector getL(Point p) {
        if (p.equals(_position)) {
            return null;
        }
        return p.subtract(_position).normalize();
    }

    /**
     * return the distance between the light and the point
     *
     * @param point the point
     * @return return the distance from light to point
     */
    @Override
    public double getDistance(Point point) {
/////maybe opposite
        return point.distance(_position);
    }
}
