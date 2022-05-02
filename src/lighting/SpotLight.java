package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Util;
import primitives.Vector;

public class SpotLight extends PointLight{
    private Vector _direction;

    public SpotLight(Color intensity, Point position, double kC, double kL, double kQ, Vector direction) {
        super(intensity, position, kC, kL, kQ);
        _direction = direction;
    }

    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        _direction = direction.normalize();
    }

@Override
public Color getIntensity(Point p) {
    Color pointIntensity = super.getIntensity(p);
    Vector l=getL(p);
    double attenuation=l.dotProduct(_direction);
    Color result = pointIntensity.scale(Math.max(0,attenuation));
    return (result);
}

}
