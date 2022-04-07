package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * Ambient Light Color
 */
public class AmbientLight {
    /**
     * intensity of ambient light color
     */
    final private Color _intensity;

    /**
     * Constructor
     * @param Ia intensity color
     * @param Ka constant for intensity
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        _intensity = Ia.scale(Ka);
    }

    public AmbientLight() {
        _intensity = Color.BLACK;
    }

    /**
     * get intensity color
     * @return intensity
     */
    public Color getIntensity() {
        return _intensity;
    }

}
