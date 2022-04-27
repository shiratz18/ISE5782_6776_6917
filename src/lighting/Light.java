package lighting;

import primitives.Color;

/**
 * class presents the source of the light
 */
abstract class Light {
    /**
     *  intensity of ambient light color
     */
    protected Color _intensity;

    /**
     * constructor
     * @param intensity of the color
     */
    protected Light(Color intensity) {
        _intensity = intensity;
    }

    /**
     * getter
     * @returnn intensity of the color
     */
    public Color getIntensity() {
        return _intensity;
    }
}
