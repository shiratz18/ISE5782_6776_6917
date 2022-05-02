package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource{
    private Point _position;
    private double _kC=1;// Constant attenuation
    private double _kL=0;// Linear attenuation
    private double _kQ=0;// Quadratic attenuation

    public PointLight(Color intensity, Point position, double kC, double kL, double kQ) {
        super(intensity);
        _position = position;
        _kC = kC;
        _kL = kL;
        _kQ = kQ;
    }

    /**
     * default constructor the constant attenuation value is 1 and the other two values are 0
     * @param intensity
     * @param position
     */
    public PointLight(Color intensity,Point position) {this(intensity,position,1d,0d,0d);}

    public PointLight setKc(double kC) {
        _kC = kC;
        return this;
    }

    public PointLight setKl(double kL) {
        _kL = kL;
        return this;
    }

    public PointLight setKq(double kQ) {
        _kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double dsquared = p.distanceSquared(_position);
        double d = p.distance(_position);

        //factor of attenuation depends on distance
        double factor = _kC + _kL * d + _kQ * dsquared;
        // attenuate intensity by factor;
        Color result = _intensity.reduce(factor);

        return result;
    }

    @Override
    public Vector getL(Point p) {
        if (p.equals(_position)) {
            return null;
        }
        return p.subtract(_position).normalize();
    }
}
